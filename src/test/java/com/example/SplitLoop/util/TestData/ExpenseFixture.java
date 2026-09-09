package com.example.SplitLoop.util.TestData;

import com.example.SplitLoop.expense.domain.entity.*;
import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.util.mother.*;

import java.util.List;

public final class ExpenseFixture {

    private ExpenseFixture() {
    }

    public static ExpenseContext defaultContext() {

        User owner = UserMother.user();

        User member = UserMother.anotherUser();

        Group group = GroupMother.group(owner);

        GroupMember admin = GroupMemberMother.admin(group, owner);

        GroupMember groupMember = GroupMemberMother.member(group, member);

        RecurringExpense recurringExpense = RecurringExpenseMother.active(group, owner);

        ExpenseOccurrence occurrence = ExpenseOccurrenceMother.pending(recurringExpense);

        RecurringExpenseParticipant participant1 = RecurringExpenseParticipantMother.participant(recurringExpense, owner);

        RecurringExpenseParticipant participant2 = RecurringExpenseParticipantMother.participant(recurringExpense, member);

        ExpenseOccurrenceSplit split1 = ExpenseOccurrenceSplitMother.pending(occurrence, owner);

        ExpenseOccurrenceSplit split2 = ExpenseOccurrenceSplitMother.pending(occurrence, member);

        return ExpenseContext.builder()
                .owner(owner)
                .secondUser(member)
                .group(group)
                .admin(admin)
                .member(groupMember)
                .recurringExpense(recurringExpense)
                .participants(List.of(participant1, participant2))
                .occurrence(occurrence)
                .splits(List.of(split1, split2))
                .build();
    }
}