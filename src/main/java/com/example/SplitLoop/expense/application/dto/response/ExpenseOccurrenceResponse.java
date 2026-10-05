package com.example.SplitLoop.expense.application.dto.response;

import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Builder;


@Builder
public record ExpenseOccurrenceResponse(
        UUID id,
        UUID recurringExpenseId,
        UUID groupId,
        String name,
        BigDecimal amount,
        UUID paidBy,
        LocalDate dueDate,
        LocalDate periodStart,
        LocalDate periodEnd,
        ExpenseOccurrenceStatus status,
        LocalDateTime createdAt,
        List<ExpenseOccurrenceSplitResponse> splits
) {}
