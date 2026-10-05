package com.example.SplitLoop.expense.application.usecase.command;

import com.example.SplitLoop.expense.application.dto.mapper.ExpenseDtoMapper;
import com.example.SplitLoop.expense.domain.exception.UserNotMemberOfGroupException;
import com.example.SplitLoop.expense.domain.model.RecurringExpense;
import com.example.SplitLoop.expense.domain.port.GroupMemberPort;
import com.example.SplitLoop.expense.domain.service.ExpenseOccurrenceService;
import com.example.SplitLoop.common.application.service.CurrentUserService;
import com.example.SplitLoop.expense.application.dto.response.RecurringExpenseResponse;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.expense.domain.exception.RecurringExpenseNotFoundException;
import com.example.SplitLoop.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DuplicateRecurringExpenseUseCase {

    private final RecurringExpenseRepository recurringExpenseRepository;
    private final GroupMemberPort groupMemberPort;
    private final CurrentUserService currentUserService;
    private final ExpenseOccurrenceService occurrenceService;
    private final ExpenseDtoMapper mapper;

    @Transactional
    public RecurringExpenseResponse execute(UUID recurringExpenseId) {

        // 1. Obtener el gasto recurrente original
        RecurringExpense original = recurringExpenseRepository.findById(recurringExpenseId)
                .orElseThrow(() -> new RecurringExpenseNotFoundException(recurringExpenseId));

        // 2. Obtener usuario actual y validar pertenencia al grupo
        User currentUser = currentUserService.getCurrentUser();

        groupMemberPort.findByGroupIdAndUserId(original.group().id(), currentUser.id())
                .orElseThrow(() -> new UserNotMemberOfGroupException(currentUser.id()));

        // 3. Crear el nuevo objeto duplicado desde el Dominio
        RecurringExpense duplicate = original.duplicate(currentUser, original.startDate());

        // 4. Guardar la nueva entidad duplicada (y sus participantes en cascada/repositorio)
        RecurringExpense savedDuplicate = recurringExpenseRepository.save(duplicate);

        // 5. Generar las ocurrencias pendientes asociadas a la nueva entidad duplicada
        occurrenceService.generatePendingOccurrences(savedDuplicate, savedDuplicate.startDate());

        // 6. Retornar el DTO de respuesta
        return mapper.toResponse(savedDuplicate);
    }
}