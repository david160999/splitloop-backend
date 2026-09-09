package com.example.SplitLoop.expense.application.command;

import com.example.SplitLoop.user.domain.service.CurrentUserService;
import com.example.SplitLoop.expense.controller.command.CreateRecurringExpenseCommand;
import com.example.SplitLoop.expense.controller.response.RecurringExpenseResponse;
import com.example.SplitLoop.expense.domain.entity.RecurringExpense;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseParticipant;
import com.example.SplitLoop.expense.domain.service.RecurringExpenseService;
import com.example.SplitLoop.expense.mapper.RecurringExpenseMapper;
import com.example.SplitLoop.expense.mapper.RecurringExpenseParticipantMapper;
import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.group.domain.repository.GroupMemberRepository;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.exception.GroupNotFoundException;
import com.example.SplitLoop.group.exception.UserNotFoundException;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateRecurringExpenseUseCase {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;

    private final RecurringExpenseService recurringExpenseService;
    private final RecurringExpenseMapper mapper;
    private final RecurringExpenseParticipantMapper participantMapper;

    private final CurrentUserService currentUserService;

    @Transactional
    public RecurringExpenseResponse execute(CreateRecurringExpenseCommand request) {

        User createdBy = currentUserService.getCurrentUser();

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new GroupNotFoundException(request.getGroupId()));

        User paidBy = userRepository.findById(request.getPaidById())
                .orElseThrow(() -> new UserNotFoundException(request.getPaidById()));

        List<GroupMember> members = groupMemberRepository.findAllByGroup(group);

        RecurringExpense recurringExpense =
                mapper.toEntity(
                        request,
                        group,
                        paidBy,
                        createdBy);

        List<RecurringExpenseParticipant> participants = participantMapper.toEntities(request.getParticipants());

        RecurringExpense savedExpense =
                recurringExpenseService.createRecurringExpense(
                        recurringExpense,
                        participants,
                        members);

        RecurringExpenseResponse response = mapper.toResponse(savedExpense);

        response.setParticipants(mapper.toParticipantResponses(savedExpense.getParticipants()));

        return response;
    }
}
