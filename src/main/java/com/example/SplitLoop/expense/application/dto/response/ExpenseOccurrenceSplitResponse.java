package com.example.SplitLoop.expense.application.dto.response;

import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceSplitStatus;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;

@Builder
public record ExpenseOccurrenceSplitResponse(
        UUID id,
        UUID userId,
        BigDecimal amountOwed,
        BigDecimal amountPaid,
        ExpenseOccurrenceSplitStatus status
) {}