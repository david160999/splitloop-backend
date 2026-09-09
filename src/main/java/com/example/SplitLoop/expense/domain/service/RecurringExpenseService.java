package com.example.SplitLoop.expense.domain.service;

import com.example.SplitLoop.expense.domain.entity.RecurringExpense;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseParticipant;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.user.domain.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface RecurringExpenseService {

    RecurringExpense createRecurringExpense(
            RecurringExpense recurringExpense,
            List<RecurringExpenseParticipant> participants,
            List<GroupMember> groupMembers);

    RecurringExpense updateRecurringExpense(
            RecurringExpense recurringExpense,
            List<RecurringExpenseParticipant> participants,
            List<GroupMember> groupMembers);

    void deleteRecurringExpense(RecurringExpense recurringExpense, GroupMember member);

    void pause(RecurringExpense recurringExpense, GroupMember member);

    void resume(RecurringExpense recurringExpense, GroupMember member);

    RecurringExpense duplicateRecurringExpense(
            RecurringExpense original,
            List<RecurringExpenseParticipant> participants,
            User createdBy,
            LocalDate startDate,
            List<GroupMember> groupMembers);

    void completeIfExpired(RecurringExpense recurringExpense, LocalDate today);
}
