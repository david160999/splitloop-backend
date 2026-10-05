package com.example.SplitLoop.util.TestData;

import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceSplitEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.RecurringExpenseEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.RecurringExpenseParticipantEntity;
import com.example.SplitLoop.group.infrastructure.persistence.entity.GroupEntity;
import com.example.SplitLoop.group.infrastructure.persistence.entity.GroupMemberEntity;
import com.example.SplitLoop.user.infrastructure.persistence.entity.UserEntity;
import com.example.SplitLoop.util.mother.*;

import java.util.List;

public final class ExpenseFixture {

    private ExpenseFixture() {
    }

    public static ExpenseContext defaultContext() {

        UserEntity owner = UserMother.userEntity();

        UserEntity member = UserMother.anotherUserEntity();

        GroupEntity groupEntity = GroupMother.group(owner);

        GroupMemberEntity admin = GroupMemberMother.admin(groupEntity, owner);

        GroupMemberEntity groupMemberEntity = GroupMemberMother.member(groupEntity, member);

        RecurringExpenseEntity recurringExpenseEntity = RecurringExpenseMother.active(groupEntity, owner);

        ExpenseOccurrenceEntity occurrence = ExpenseOccurrenceMother.pending(recurringExpenseEntity);

        RecurringExpenseParticipantEntity participant1 = RecurringExpenseParticipantMother.participant(recurringExpenseEntity, owner);

        RecurringExpenseParticipantEntity participant2 = RecurringExpenseParticipantMother.participant(recurringExpenseEntity, member);

        ExpenseOccurrenceSplitEntity split1 = ExpenseOccurrenceSplitMother.pending(occurrence, owner);

        ExpenseOccurrenceSplitEntity split2 = ExpenseOccurrenceSplitMother.pending(occurrence, member);

        return ExpenseContext.builder()
                .owner(owner)
                .secondUserEntity(member)
                .groupEntity(groupEntity)
                .admin(admin)
                .member(groupMemberEntity)
                .recurringExpenseEntity(recurringExpenseEntity)
                .participants(List.of(participant1, participant2))
                .occurrence(occurrence)
                .splits(List.of(split1, split2))
                .build();
    }
}