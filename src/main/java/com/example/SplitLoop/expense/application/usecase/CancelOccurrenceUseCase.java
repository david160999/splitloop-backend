package com.example.SplitLoop.expense.application.usecase;

import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.expense.domain.service.ExpenseOccurrenceService;
import com.example.SplitLoop.expense.exception.ExpenseOccurrenceNotFoundException;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.group.domain.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CancelOccurrenceUseCase {

    private final ExpenseOccurrenceRepository occurrenceRepository;
    private final ExpenseOccurrenceService occurrenceService;
    private final GroupService groupService;

    @Transactional
    public void execute(UUID occurrenceId) {

        ExpenseOccurrence occurrence = occurrenceRepository.findById(occurrenceId)
                .orElseThrow(() -> new ExpenseOccurrenceNotFoundException(occurrenceId));

        GroupMember member = groupService.getCurrentMember(occurrence.getGroup());

        occurrenceService.cancelOccurrence(occurrence, member);
    }
}