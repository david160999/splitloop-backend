package com.example.SplitLoop.expense.application.usecase.command;

import com.example.SplitLoop.common.application.service.CurrentUserService;
import com.example.SplitLoop.expense.domain.exception.UserNotMemberOfGroupException;
import com.example.SplitLoop.expense.domain.model.RecurringExpense;
import com.example.SplitLoop.expense.domain.port.GroupMemberPort;
import com.example.SplitLoop.expense.domain.service.ExpenseOccurrenceService;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.expense.domain.exception.RecurringExpenseNotFoundException;
import com.example.SplitLoop.group.domain.model.GroupMember;
import com.example.SplitLoop.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteRecurringExpenseUseCase {

    private final RecurringExpenseRepository recurringExpenseRepository;
    private final GroupMemberPort groupMemberPort;
    private final CurrentUserService currentUserService;
    private final ExpenseOccurrenceService occurrenceService;


    @Transactional
    public void execute(UUID recurringExpenseId) {

        RecurringExpense recurringExpense = recurringExpenseRepository.findById(recurringExpenseId)
                .orElseThrow(() -> new RecurringExpenseNotFoundException(recurringExpenseId));

        User currentUser = currentUserService.getCurrentUser();

        GroupMember member = groupMemberPort.findByGroupIdAndUserId(recurringExpense.group().id(), currentUser.id())
                .orElseThrow(() -> new UserNotMemberOfGroupException(currentUser.id()));

        member.ensureIsAdmin();
        recurringExpense.delete();

        recurringExpenseRepository.save(recurringExpense);

        occurrenceService.cancelFutureOccurrences(recurringExpense);
    }
}