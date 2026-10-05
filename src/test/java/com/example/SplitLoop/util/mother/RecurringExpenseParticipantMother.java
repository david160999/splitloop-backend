package com.example.SplitLoop.util.mother;

import com.example.SplitLoop.expense.infrastructure.persistence.entity.RecurringExpenseEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.RecurringExpenseParticipantEntity;
import com.example.SplitLoop.user.infrastructure.persistence.entity.UserEntity;

import java.math.BigDecimal;
import java.util.UUID;

public final class RecurringExpenseParticipantMother {

    private RecurringExpenseParticipantMother() {
    }

    public static RecurringExpenseParticipantEntity participant() {

        UserEntity userEntity = UserMother.userEntity();
        RecurringExpenseEntity recurringExpenseEntity = RecurringExpenseMother.active();

        return participant(recurringExpenseEntity, userEntity);
    }

    public static RecurringExpenseParticipantEntity participant(RecurringExpenseEntity recurringExpenseEntity, UserEntity userEntity) {

        return RecurringExpenseParticipantEntity.builder()
                .id(UUID.randomUUID())
                .recurringExpense(recurringExpenseEntity)
                .user(userEntity)
                .value(null)
                .build();
    }

    public static RecurringExpenseParticipantEntity percentage(
            RecurringExpenseEntity recurringExpenseEntity,
            UserEntity userEntity,
            BigDecimal percentage) {

        return participant(recurringExpenseEntity, userEntity)
                .toBuilder()
                .value(percentage)
                .build();
    }

    public static RecurringExpenseParticipantEntity fixed(
            RecurringExpenseEntity recurringExpenseEntity,
            UserEntity userEntity,
            BigDecimal amount) {

        return participant(recurringExpenseEntity, userEntity)
                .toBuilder()
                .value(amount)
                .build();
    }
}