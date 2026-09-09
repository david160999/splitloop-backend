package com.example.SplitLoop.util.mother;

import com.example.SplitLoop.expense.domain.entity.RecurringExpense;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseParticipant;
import com.example.SplitLoop.user.domain.entity.User;

import java.math.BigDecimal;
import java.util.UUID;

public final class RecurringExpenseParticipantMother {

    private RecurringExpenseParticipantMother() {
    }

    public static RecurringExpenseParticipant participant() {

        User user = UserMother.user();
        RecurringExpense recurringExpense = RecurringExpenseMother.active();

        return participant(recurringExpense, user);
    }

    public static RecurringExpenseParticipant participant(RecurringExpense recurringExpense, User user) {

        return RecurringExpenseParticipant.builder()
                .id(UUID.randomUUID())
                .recurringExpense(recurringExpense)
                .user(user)
                .value(null)
                .build();
    }

    public static RecurringExpenseParticipant percentage(
            RecurringExpense recurringExpense,
            User user,
            BigDecimal percentage) {

        return participant(recurringExpense, user)
                .toBuilder()
                .value(percentage)
                .build();
    }

    public static RecurringExpenseParticipant fixed(
            RecurringExpense recurringExpense,
            User user,
            BigDecimal amount) {

        return participant(recurringExpense, user)
                .toBuilder()
                .value(amount)
                .build();
    }
}