package com.example.SplitLoop.expense.domain.validator;

import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.entity.RecurringExpense;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseParticipant;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.user.domain.entity.User;

import java.util.List;

public interface ExpenseValidator {

    void validateRecurringExpense(RecurringExpense recurringExpense);

    void validateParticipants(
            List<GroupMember> groupMembers,
            List<RecurringExpenseParticipant> participants);

    void validateOccurrence(ExpenseOccurrence occurrence);

    void validateSplitConfiguration(
            RecurringExpense recurringExpense,
            List<RecurringExpenseParticipant> participants);

    void validateCanCancel(ExpenseOccurrence occurrence, GroupMember currentUser);

    void validateCanChangePaidBy(ExpenseOccurrence occurrence, User newPaidBy, GroupMember member);

    void validateCanPause(RecurringExpense recurringExpense, GroupMember member);

    void validateCanResume(RecurringExpense recurringExpense, GroupMember member);

    void validateCanReopen(ExpenseOccurrence occurrence, GroupMember member);

    void validateCanCancelFuture(ExpenseOccurrence occurrence, GroupMember member);

    void validateCanDelete(RecurringExpense recurringExpense, GroupMember member);
}