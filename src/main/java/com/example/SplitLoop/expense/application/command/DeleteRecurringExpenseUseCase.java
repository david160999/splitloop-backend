package com.example.SplitLoop.expense.application.command;

import com.example.SplitLoop.expense.domain.entity.RecurringExpense;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.expense.domain.service.RecurringExpenseService;
import com.example.SplitLoop.expense.exception.RecurringExpenseNotFoundException;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.group.domain.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteRecurringExpenseUseCase {

    private final RecurringExpenseRepository recurringExpenseRepository;
    private final GroupService groupService;
    private final RecurringExpenseService recurringExpenseService;

    @Transactional
    public void execute(UUID recurringExpenseId) {

        RecurringExpense recurringExpense = recurringExpenseRepository.findById(recurringExpenseId)
                .orElseThrow(() -> new RecurringExpenseNotFoundException(recurringExpenseId));

        GroupMember member = groupService.getCurrentMember(recurringExpense.getGroup());

        recurringExpenseService.deleteRecurringExpense(recurringExpense, member);
    }
}