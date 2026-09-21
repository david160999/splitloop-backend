package com.example.SplitLoop.util.mother;

import com.example.SplitLoop.expense.domain.entity.RecurringExpense;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseParticipant;
import com.example.SplitLoop.user.domain.entity.UserEntity;

import java.math.BigDecimal;
import java.util.UUID;

public final class RecurringExpenseParticipantMother {

    private RecurringExpenseParticipantMother() {
    }

    public static RecurringExpenseParticipant participant() {

        UserEntity userEntity = UserMother.userEntity();
        RecurringExpense recurringExpense = RecurringExpenseMother.active();

        return participant(recurringExpense, userEntity);
    }

    public static RecurringExpenseParticipant participant(RecurringExpense recurringExpense, UserEntity userEntity) {

        return RecurringExpenseParticipant.builder()
                .id(UUID.randomUUID())
                .recurringExpense(recurringExpense)
                .user(userEntity)
                .value(null)
                .build();
    }

    public static RecurringExpenseParticipant percentage(
            RecurringExpense recurringExpense,
            UserEntity userEntity,
            BigDecimal percentage) {

        return participant(recurringExpense, userEntity)
                .toBuilder()
                .value(percentage)
                .build();
    }

    public static RecurringExpenseParticipant fixed(
            RecurringExpense recurringExpense,
            UserEntity userEntity,
            BigDecimal amount) {

        return participant(recurringExpense, userEntity)
                .toBuilder()
                .value(amount)
                .build();
    }
}