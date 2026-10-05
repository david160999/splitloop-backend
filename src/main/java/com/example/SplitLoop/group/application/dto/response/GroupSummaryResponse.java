package com.example.SplitLoop.group.application.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
public record GroupSummaryResponse(
        UUID groupId,
        String name,
        int members,
        int activeRecurringExpenses,
        int pendingOccurrences,
        BigDecimal totalExpenses,
        BigDecimal pendingAmount,
        LocalDate nextDueDate
) {}
