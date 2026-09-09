package com.example.SplitLoop.expense.application.usecase;

import com.example.SplitLoop.user.domain.service.CurrentUserService;
import com.example.SplitLoop.expense.controller.response.RecurringExpenseResponse;
import com.example.SplitLoop.expense.domain.entity.RecurringExpense;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseParticipant;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseParticipantRepository;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.expense.domain.service.RecurringExpenseService;
import com.example.SplitLoop.expense.exception.RecurringExpenseNotFoundException;
import com.example.SplitLoop.expense.mapper.RecurringExpenseMapper;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.group.domain.service.GroupService;
import com.example.SplitLoop.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DuplicateRecurringExpenseUseCase {

    private final RecurringExpenseRepository recurringExpenseRepository;
    private final RecurringExpenseParticipantRepository participantRepository;

    private final CurrentUserService currentUserService;
    private final GroupService groupService;

    private final RecurringExpenseService recurringExpenseService;

    private final RecurringExpenseMapper mapper;

    @Transactional
    public RecurringExpenseResponse execute(UUID recurringExpenseId) {

        RecurringExpense original = recurringExpenseRepository.findById(recurringExpenseId)
                .orElseThrow(() -> new RecurringExpenseNotFoundException(recurringExpenseId));

        User currentUser = currentUserService.getCurrentUser();

        List<GroupMember> members = groupService.getMembers(original.getGroup());

        List<RecurringExpenseParticipant> participants = participantRepository.findByRecurringExpense(original);

        RecurringExpense duplicate =
                recurringExpenseService.duplicateRecurringExpense(
                        original,
                        participants,
                        currentUser,
                        original.getStartDate(),
                        members);

        return mapper.toResponse(duplicate);
    }
}