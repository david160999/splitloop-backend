package com.example.SplitLoop.util.mother;

import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceStatus;
import com.example.SplitLoop.expense.domain.entity.RecurringExpense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public final class ExpenseOccurrenceMother {

    private ExpenseOccurrenceMother() {
    }

    public static ExpenseOccurrence pending() {

        return pending(RecurringExpenseMother.active());
    }

    public static ExpenseOccurrence pending(
            RecurringExpense recurringExpense) {

        return ExpenseOccurrence.builder()
                .id(UUID.randomUUID())
                .recurringExpense(recurringExpense)
                .group(recurringExpense.getGroup())
                .name(recurringExpense.getName())
                .amount(recurringExpense.getAmount())
                .paidBy(recurringExpense.getPaidBy())
                .dueDate(LocalDate.of(2025, 1, 1))
                .periodStart(LocalDate.of(2025, 1, 1))
                .periodEnd(LocalDate.of(2025, 1, 31))
                .status(ExpenseOccurrenceStatus.PENDING)
                .build();
    }

    public static ExpenseOccurrence paid() {

        return pending()
                .toBuilder()
                .status(ExpenseOccurrenceStatus.PAID)
                .build();
    }

    public static ExpenseOccurrence partiallyPaid() {

        return pending()
                .toBuilder()
                .status(ExpenseOccurrenceStatus.PARTIALLY_PAID)
                .build();
    }

    public static ExpenseOccurrence cancelled() {

        return pending()
                .toBuilder()
                .status(ExpenseOccurrenceStatus.CANCELLED)
                .build();
    }
}
