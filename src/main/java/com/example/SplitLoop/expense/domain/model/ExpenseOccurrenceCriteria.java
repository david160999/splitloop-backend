package com.example.SplitLoop.expense.domain.model;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record ExpenseOccurrenceCriteria(
        UUID recurringExpenseId,
        ExpenseOccurrenceStatus status,
        LocalDate from,
        LocalDate to
) {}
