package com.example.SplitLoop.expense.application.usecase.command;

import com.example.SplitLoop.common.application.service.CurrentUserService;
import com.example.SplitLoop.expense.application.dto.mapper.ExpenseDtoMapper;
import com.example.SplitLoop.expense.application.dto.response.ExpenseOccurrenceResponse;
import com.example.SplitLoop.expense.domain.exception.UserNotMemberOfGroupException;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.port.GroupMemberPort;
import com.example.SplitLoop.expense.domain.service.ExpenseOccurrenceSplitService;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.expense.domain.exception.ExpenseOccurrenceNotFoundException;
import com.example.SplitLoop.group.domain.model.GroupMember;
import com.example.SplitLoop.group.domain.exception.UserNotFoundException;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChangePaidByUseCase {

    private final ExpenseOccurrenceRepository occurrenceRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final GroupMemberPort groupMemberPort;
    private final ExpenseDtoMapper mapper;
    private final ExpenseOccurrenceSplitService splitService;

    @Transactional
    public ExpenseOccurrenceResponse execute(UUID occurrenceId, UUID newPaidById) {

        // 1. Cargar agregados/modelos de dominio
        ExpenseOccurrence occurrence = occurrenceRepository.findById(occurrenceId)
                .orElseThrow(() -> new ExpenseOccurrenceNotFoundException(occurrenceId));

        User newPaidBy = userRepository.findById(newPaidById)
                .orElseThrow(() -> new UserNotFoundException(newPaidById));

        User currentUser = currentUserService.getCurrentUser();

        // 2. Validar que el usuario actual y el nuevo pagador pertenezcan al grupo, y que el usuario que emite la accion sea Admin.
        GroupMember currentMember = groupMemberPort.findByGroupIdAndUserId(occurrence.group().id(), currentUser.id())
                .orElseThrow(() -> new UserNotMemberOfGroupException(currentUser.id()));

        groupMemberPort.findByGroupIdAndUserId(occurrence.group().id(), newPaidBy.id())
                .orElseThrow(() -> new UserNotMemberOfGroupException(newPaidBy.id()));

        currentMember.ensureIsAdmin();

        // 3. Mutar el pagador en el modelo inmutable de dominio
        ExpenseOccurrence updatedOccurrence = occurrence.changePaidBy(newPaidBy);

        // 4. Guardar los cambios en la ocurrencia
        ExpenseOccurrence savedOccurrence = occurrenceRepository.save(updatedOccurrence);

        // 5. Recalcular y persistir explícitamente los desgloses/splits para la ocurrencia
        splitService.recalculateParticipants(savedOccurrence);

        // 6. Retornar DTO de respuesta
        return mapper.toResponse(savedOccurrence);
    }
}
