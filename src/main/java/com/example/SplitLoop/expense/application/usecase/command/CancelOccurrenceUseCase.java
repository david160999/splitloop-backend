package com.example.SplitLoop.expense.application.usecase.command;

import com.example.SplitLoop.common.application.service.CurrentUserService;
import com.example.SplitLoop.expense.domain.exception.UserNotMemberOfGroupException;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.port.GroupMemberPort;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceSplitRepository;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.expense.domain.exception.ExpenseOccurrenceNotFoundException;
import com.example.SplitLoop.group.domain.model.GroupMember;
import com.example.SplitLoop.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CancelOccurrenceUseCase {

    private final ExpenseOccurrenceRepository occurrenceRepository;
    private final GroupMemberPort groupMemberPort;
    private final CurrentUserService currentUserService;
    private final ExpenseOccurrenceSplitRepository splitRepository;

    @Transactional
    public void execute(UUID occurrenceId) {

        // 1. Obtener la ocurrencia
        ExpenseOccurrence occurrence = occurrenceRepository.findById(occurrenceId)
                .orElseThrow(() -> new ExpenseOccurrenceNotFoundException(occurrenceId));

        // 2. Obtener usuario actual y validar permisos de grupo
        User currentUser = currentUserService.getCurrentUser();

        GroupMember member = groupMemberPort.findByGroupIdAndUserId(occurrence.group().id(), currentUser.id())
                .orElseThrow(() -> new UserNotMemberOfGroupException(currentUser.id()));

        //Solo administradores o creadores pueden cancelar
        member.ensureIsAdmin();

        // 3. Modificar el estado en el Dominio (valida sus propias reglas e inmutabilidad)
        ExpenseOccurrence cancelledOccurrence = occurrence.cancel();

        // 4. Persistir la ocurrencia cancelada
        ExpenseOccurrence savedOccurrence = occurrenceRepository.save(cancelledOccurrence);

        // 5. Cancelar/actualizar el estado de los splits asociados
        splitRepository.cancelSplitsForOccurrence(savedOccurrence.id());

    }

}