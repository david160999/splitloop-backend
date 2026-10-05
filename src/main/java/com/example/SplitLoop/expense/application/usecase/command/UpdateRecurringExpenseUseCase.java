package com.example.SplitLoop.expense.application.usecase.command;

import com.example.SplitLoop.expense.application.dto.request.UpdateRecurringExpenseRequest;
import com.example.SplitLoop.expense.application.dto.mapper.ExpenseDtoMapper;
import com.example.SplitLoop.expense.application.dto.response.RecurringExpenseResponse;
import com.example.SplitLoop.expense.domain.model.Money;
import com.example.SplitLoop.expense.domain.model.RecurringExpense;
import com.example.SplitLoop.expense.domain.model.RecurringExpenseParticipant;
import com.example.SplitLoop.expense.domain.policy.ExpenseSplitPolicy;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.expense.domain.exception.RecurringExpenseNotFoundException;
import com.example.SplitLoop.group.domain.model.GroupMember;
import com.example.SplitLoop.group.domain.repository.GroupMemberRepository;
import com.example.SplitLoop.group.domain.exception.UserNotFoundException;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateRecurringExpenseUseCase {

    private final RecurringExpenseRepository recurringExpenseRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    private final ExpenseDtoMapper mapper;

    @Transactional
    public RecurringExpenseResponse execute(UUID recurringExpenseId, UpdateRecurringExpenseRequest request) {

        // 1. Cargar el agregado de gasto recurrente
        RecurringExpense currentExpense = recurringExpenseRepository.findById(recurringExpenseId)
                .orElseThrow(() -> new RecurringExpenseNotFoundException(recurringExpenseId));

        // 2. Cargar entidades relacionales si se enviaron en la petición
        User paidBy = null;
        if (request.getPaidById() != null) {
            paidBy = userRepository.findById(request.getPaidById())
                    .orElseThrow(() -> new UserNotFoundException(request.getPaidById()));
        }

        // 3. Obtener los miembros del grupo utilizando el Puerto de salida
        List<GroupMember> groupMembers = groupMemberRepository.findByGroupId(currentExpense.group().id());

        // 4. Mapear DTOs de participantes a objetos de Dominio
        Map<UUID, User> groupUsersById = groupMembers.stream()
                .map(GroupMember::user)
                .collect(Collectors.toMap(User::id, Function.identity()));

        List<RecurringExpenseParticipant> newParticipants = request.getParticipants().stream()
                .map(req -> {
                    User user = Optional.ofNullable(groupUsersById.get(req.getUserId()))
                            .orElseThrow(() -> new UserNotFoundException(req.getUserId()));
                    return mapper.toParticipantDomain(req, user);
                })
                .toList();

        // 5. Aplicar la actualización inmutable en el agregado (Aplica guardas de estado)
        RecurringExpense updatedExpense = currentExpense.update(
                request.getName(),
                request.getDescription(),
                request.getAmount() != null ? new Money(request.getAmount()) : null,
                request.getFrequency(),
                request.getSplitType(),
                request.getStartDate(),
                request.getEndDate(),
                paidBy,
                newParticipants
        );

        // 6. Validar las reglas de negocio del reparto (Split Policy / Service de Dominio)
        ExpenseSplitPolicy.validateParticipants(groupMembers, newParticipants);
        ExpenseSplitPolicy.validateSplitConfiguration(updatedExpense, newParticipants);

        // 7. Persistir el agregado actualizado
        RecurringExpense savedExpense = recurringExpenseRepository.save(updatedExpense);

        // 8. Retornar DTO de respuesta
        return mapper.toResponse(savedExpense);
    }
}