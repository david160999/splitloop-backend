package com.example.SplitLoop.group.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupSummaryResponse {

    private UUID id;

    private String name;

    private int members;

    private int activeRecurringExpenses;

    private int pendingOccurrences;

    private BigDecimal totalExpenses;

    private BigDecimal pendingAmount;

    private LocalDate nextDueDate;
}
