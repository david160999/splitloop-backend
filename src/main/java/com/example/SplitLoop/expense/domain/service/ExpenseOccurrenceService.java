package com.example.SplitLoop.expense.domain.service;

import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.entity.RecurringExpense;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.user.domain.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseOccurrenceService {

    void updateFutureOccurrences(
            RecurringExpense recurringExpense);

    ExpenseOccurrence updateOccurrence(
            ExpenseOccurrence occurrence);

    void cancelOccurrence(
            ExpenseOccurrence occurrence,
            GroupMember member);

    void reopen(ExpenseOccurrence occurrence, GroupMember member);

    ExpenseOccurrence changePaidBy(ExpenseOccurrence occurrence, User newPaidBy, GroupMember member);

    void cancelFutureOccurrences(RecurringExpense recurringExpense, GroupMember member);

    List<ExpenseOccurrence> generatePendingOccurrences(RecurringExpense recurringExpense, LocalDate until);
}
