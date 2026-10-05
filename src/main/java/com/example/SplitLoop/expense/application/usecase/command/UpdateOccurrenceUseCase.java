package com.example.SplitLoop.expense.application.usecase.command;

import com.example.SplitLoop.expense.application.dto.request.UpdateOccurrenceRequest;
import com.example.SplitLoop.expense.application.dto.mapper.ExpenseDtoMapper;
import com.example.SplitLoop.expense.application.dto.response.ExpenseOccurrenceResponse;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.model.Money;
import com.example.SplitLoop.expense.domain.service.ExpenseOccurrenceSplitService;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.expense.domain.exception.ExpenseOccurrenceNotFoundException;
import com.example.SplitLoop.group.domain.exception.UserNotFoundException;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateOccurrenceUseCase {

    private final ExpenseOccurrenceRepository occurrenceRepository;
    private final UserRepository userRepository;
    private final ExpenseOccurrenceSplitService splitService;

    private final ExpenseDtoMapper mapper;

    @Transactional
    public ExpenseOccurrenceResponse execute(UUID occurrenceId, UpdateOccurrenceRequest request) {

        // 1. Obtención de agregados y datos del contexto
        ExpenseOccurrence occurrence = occurrenceRepository.findById(occurrenceId)
                .orElseThrow(() -> new ExpenseOccurrenceNotFoundException(occurrenceId));

        User newPaidBy = null;
        if (request.getPaidById() != null) {
            newPaidBy = userRepository.findById(request.getPaidById())
                    .orElseThrow(() -> new UserNotFoundException(request.getPaidById()));
        }

        // 2. Aplicar actualización de detalles generales (invoca ensureCanBeModified internamente)
        ExpenseOccurrence updatedOccurrence = occurrence.updateDetails(
                request.getName(),
                new Money(request.getAmount()),
                request.getDueDate(),
                newPaidBy
        );

        // 4. Persistir
        ExpenseOccurrence savedOccurrence = occurrenceRepository.save(updatedOccurrence);

        // 5. Recalcular splits con el nuevo monto/pagador si es necesario
        splitService.recalculateParticipants(savedOccurrence);

        return mapper.toResponse(savedOccurrence);
    }
}