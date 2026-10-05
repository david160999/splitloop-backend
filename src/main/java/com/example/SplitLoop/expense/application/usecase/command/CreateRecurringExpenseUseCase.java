package com.example.SplitLoop.expense.application.usecase.command;

import com.example.SplitLoop.expense.application.dto.mapper.ExpenseDtoMapper;
import com.example.SplitLoop.expense.domain.model.RecurringExpense;
import com.example.SplitLoop.expense.domain.model.RecurringExpenseParticipant;
import com.example.SplitLoop.expense.domain.policy.ExpenseSplitPolicy;
import com.example.SplitLoop.expense.domain.port.GroupMemberPort;
import com.example.SplitLoop.expense.domain.port.GroupRepositoryPort;
import com.example.SplitLoop.expense.domain.port.UserRepositoryPort;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.expense.domain.service.ExpenseOccurrenceService;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.group.domain.model.GroupMember;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.common.application.service.CurrentUserService;
import com.example.SplitLoop.expense.application.dto.request.CreateRecurringExpenseRequest;
import com.example.SplitLoop.expense.application.dto.response.RecurringExpenseResponse;
import com.example.SplitLoop.group.domain.exception.GroupNotFoundException;
import com.example.SplitLoop.group.domain.exception.UserNotFoundException;
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
public class CreateRecurringExpenseUseCase {

    // Puertos de Salida (Interfaces de Dominio, no Repositorios JPA directos)
    private final GroupMemberPort groupMemberPort;
    private final GroupRepositoryPort groupPort;
    private final UserRepositoryPort userPort;

    private final CurrentUserService currentUserService;
    private final RecurringExpenseRepository recurringExpenseRepository;
    private final ExpenseOccurrenceService occurrenceService;

    // Mappers de Aplicación / DTOs
    private final ExpenseDtoMapper mapper;

    @Transactional
    public RecurringExpenseResponse execute(CreateRecurringExpenseRequest request) {

        // 1. Obtención de datos contextuales mediante puertos
        User createdBy = currentUserService.getCurrentUser();

        Group group = groupPort.findById(request.getGroupId())
                .orElseThrow(() -> new GroupNotFoundException(request.getGroupId()));

        User paidBy = userPort.findById(request.getPaidById())
                .orElseThrow(() -> new UserNotFoundException(request.getPaidById()));

        List<GroupMember> members = groupMemberPort.findByGroupId(group.id());

        // 2. Mapeo a modelos de dominio inmutables
        RecurringExpense baseExpense = mapper.toDomain(request, group, paidBy, createdBy);

        Map<UUID, User> groupUsersById = members.stream()
                .map(GroupMember::user)
                .collect(Collectors.toMap(User::id, Function.identity()));

        List<RecurringExpenseParticipant> participants = request.getParticipants().stream()
                .map(req -> {
                    User user = Optional.ofNullable(groupUsersById.get(req.getUserId()))
                            .orElseThrow(() -> new UserNotFoundException(req.getUserId()));
                    return mapper.toParticipantDomain(req, user);
                })
                .toList();

        // 3. Validaciones de Dominio (Policies puras)
        ExpenseSplitPolicy.validateParticipants(members, participants);
        ExpenseSplitPolicy.validateSplitConfiguration(baseExpense, participants);

        // 4. Construcción del Agregado Raíz inmutable
        List<RecurringExpenseParticipant> preparedParticipants = participants.stream()
                .map(p -> p.withRecurringExpenseId(baseExpense.id()))
                .toList();

        RecurringExpense expenseToSave = baseExpense.toBuilder()
                .participants(preparedParticipants)
                .build();

        // 5. Persistencia y efectos de dominio
        RecurringExpense savedExpense = recurringExpenseRepository.save(expenseToSave);

        occurrenceService.generatePendingOccurrences(savedExpense, savedExpense.startDate());

        // 6. Retorno de DTO (MapStruct mapea la lista de participantes internamente)
        return mapper.toResponse(savedExpense);
    }
}