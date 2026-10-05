package com.example.SplitLoop.util.mother;

import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceEntity;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceStatus;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.RecurringExpenseEntity;

import java.time.LocalDate;
import java.util.UUID;

public final class ExpenseOccurrenceMother {

    private ExpenseOccurrenceMother() {
    }

    public static ExpenseOccurrenceEntity pending() {

        return pending(RecurringExpenseMother.active());
    }

    public static ExpenseOccurrenceEntity pending(
            RecurringExpenseEntity recurringExpenseEntity) {

        return ExpenseOccurrenceEntity.builder()
                .id(UUID.randomUUID())
                .recurringExpense(recurringExpenseEntity)
                .group(recurringExpenseEntity.getGroup())
                .name(recurringExpenseEntity.getName())
                .amount(recurringExpenseEntity.getAmount())
                .paidBy(recurringExpenseEntity.getPaidBy())
                .dueDate(LocalDate.of(2025, 1, 1))
                .periodStart(LocalDate.of(2025, 1, 1))
                .periodEnd(LocalDate.of(2025, 1, 31))
                .status(ExpenseOccurrenceStatus.PENDING)
                .build();
    }

    public static ExpenseOccurrenceEntity paid() {

        return pending()
                .toBuilder()
                .status(ExpenseOccurrenceStatus.PAID)
                .build();
    }

    public static ExpenseOccurrenceEntity partiallyPaid() {

        return pending()
                .toBuilder()
                .status(ExpenseOccurrenceStatus.PARTIALLY_PAID)
                .build();
    }

    public static ExpenseOccurrenceEntity cancelled() {

        return pending()
                .toBuilder()
                .status(ExpenseOccurrenceStatus.CANCELLED)
                .build();
    }
}
