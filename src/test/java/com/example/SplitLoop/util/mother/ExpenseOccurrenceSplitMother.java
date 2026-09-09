package com.example.SplitLoop.util.mother;


import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplitStatus;
import com.example.SplitLoop.user.domain.entity.User;

import java.math.BigDecimal;
import java.util.UUID;

public final class ExpenseOccurrenceSplitMother {

    private ExpenseOccurrenceSplitMother() {
    }

    public static ExpenseOccurrenceSplit pending() {

        User user = UserMother.user();
        ExpenseOccurrence occurrence = ExpenseOccurrenceMother.pending();

        return pending(occurrence, user);
    }

    public static ExpenseOccurrenceSplit pending(
            ExpenseOccurrence occurrence,
            User user) {

        return ExpenseOccurrenceSplit.builder()
                .id(UUID.randomUUID())
                .occurrence(occurrence)
                .user(user)
                .amountOwed(BigDecimal.TEN)
                .amountPaid(BigDecimal.ZERO)
                .status(ExpenseOccurrenceSplitStatus.PENDING)
                .build();
    }

    public static ExpenseOccurrenceSplit partiallyPaid() {

        return pending()
                .toBuilder()
                .amountPaid(BigDecimal.valueOf(5))
                .status(ExpenseOccurrenceSplitStatus.PARTIALLY_PAID)
                .build();
    }

    public static ExpenseOccurrenceSplit paid() {

        return pending()
                .toBuilder()
                .amountPaid(BigDecimal.TEN)
                .status(ExpenseOccurrenceSplitStatus.PAID)
                .build();
    }
}