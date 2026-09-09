package com.example.SplitLoop.expense.application.usecase;

import com.example.SplitLoop.expense.controller.response.ExpenseOccurrenceResponse;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.expense.domain.service.ExpenseOccurrenceService;
import com.example.SplitLoop.expense.exception.ExpenseOccurrenceNotFoundException;
import com.example.SplitLoop.expense.mapper.ExpenseOccurrenceMapper;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.group.domain.service.GroupService;
import com.example.SplitLoop.group.exception.UserNotFoundException;
import com.example.SplitLoop.user.domain.entity.User;
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

    private final GroupService groupService;
    private final ExpenseOccurrenceService occurrenceService;

    private final ExpenseOccurrenceMapper mapper;

    @Transactional
    public ExpenseOccurrenceResponse execute(UUID occurrenceId, UUID paidById) {

        ExpenseOccurrence occurrence = occurrenceRepository.findById(occurrenceId)
                .orElseThrow(() -> new ExpenseOccurrenceNotFoundException(occurrenceId));

        User newPaidBy = userRepository.findById(paidById)
                .orElseThrow(() -> new UserNotFoundException(paidById));

        GroupMember member = groupService.getCurrentMember(occurrence.getGroup());

        occurrenceService.changePaidBy(
                occurrence,
                newPaidBy,
                member);

        return mapper.toResponse(occurrence);
    }
}
