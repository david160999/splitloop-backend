package com.example.SplitLoop.group.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OccurrenceSummaryData(
        int pendingCount,
        BigDecimal totalAmount,
        LocalDate nextDueDate
) {
    public OccurrenceSummaryData {
        if (pendingCount < 0) {
            throw new IllegalArgumentException("pendingCount cannot be negative");
        }
        totalAmount = totalAmount != null ? totalAmount : BigDecimal.ZERO;
    }
}
