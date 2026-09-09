package com.example.SplitLoop.expense.application.command;

import com.example.SplitLoop.expense.controller.command.UpdateRecurringExpenseCommand;
import com.example.SplitLoop.expense.controller.response.RecurringExpenseResponse;
import com.example.SplitLoop.expense.domain.entity.RecurringExpense;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseParticipant;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.expense.domain.service.RecurringExpenseService;
import com.example.SplitLoop.expense.exception.RecurringExpenseNotFoundException;
import com.example.SplitLoop.expense.mapper.RecurringExpenseMapper;
import com.example.SplitLoop.expense.mapper.RecurringExpenseParticipantMapper;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.group.domain.repository.GroupMemberRepository;
import com.example.SplitLoop.group.exception.UserNotFoundException;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateRecurringExpenseUseCase {

    private final RecurringExpenseRepository recurringExpenseRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    private final RecurringExpenseParticipantMapper participantMapper;

    private final RecurringExpenseService recurringExpenseService;

    private final RecurringExpenseMapper mapper;

    @Transactional
    public RecurringExpenseResponse execute(UUID recurringExpenseId, UpdateRecurringExpenseCommand request) {

        RecurringExpense recurringExpense = recurringExpenseRepository.findById(recurringExpenseId)
                .orElseThrow(() -> new RecurringExpenseNotFoundException(recurringExpenseId));

        User paidBy = userRepository.findById(request.getPaidById())
                .orElseThrow(() -> new UserNotFoundException(request.getPaidById()));

        mapper.updateEntity(request, recurringExpense, paidBy);

        List<GroupMember> members = groupMemberRepository.findAllByGroup(recurringExpense.getGroup());

        List<RecurringExpenseParticipant> participants = participantMapper.toEntities(request.getParticipants());

        RecurringExpense updated =
                recurringExpenseService.updateRecurringExpense(
                        recurringExpense,
                        participants,
                        members);

        return mapper.toResponse(updated);
    }
}