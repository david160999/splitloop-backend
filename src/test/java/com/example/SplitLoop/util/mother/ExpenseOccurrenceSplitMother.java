package com.example.SplitLoop.util.mother;


import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceSplitEntity;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceSplitStatus;
import com.example.SplitLoop.user.infrastructure.persistence.entity.UserEntity;

import java.math.BigDecimal;
import java.util.UUID;

public final class ExpenseOccurrenceSplitMother {

    private ExpenseOccurrenceSplitMother() {
    }

    public static ExpenseOccurrenceSplitEntity pending() {

        UserEntity userEntity = UserMother.userEntity();
        ExpenseOccurrenceEntity occurrence = ExpenseOccurrenceMother.pending();

        return pending(occurrence, userEntity);
    }

    public static ExpenseOccurrenceSplitEntity pending(
            ExpenseOccurrenceEntity occurrence,
            UserEntity userEntity) {

        return ExpenseOccurrenceSplitEntity.builder()
                .id(UUID.randomUUID())
                .occurrence(occurrence)
                .user(userEntity)
                .amountOwed(BigDecimal.TEN)
                .amountPaid(BigDecimal.ZERO)
                .status(ExpenseOccurrenceSplitStatus.PENDING)
                .build();
    }

    public static ExpenseOccurrenceSplitEntity partiallyPaid() {

        return pending()
                .toBuilder()
                .amountPaid(BigDecimal.valueOf(5))
                .status(ExpenseOccurrenceSplitStatus.PARTIALLY_PAID)
                .build();
    }

    public static ExpenseOccurrenceSplitEntity paid() {

        return pending()
                .toBuilder()
                .amountPaid(BigDecimal.TEN)
                .status(ExpenseOccurrenceSplitStatus.PAID)
                .build();
    }
}