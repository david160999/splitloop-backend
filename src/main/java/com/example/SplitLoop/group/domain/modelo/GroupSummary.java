package com.example.SplitLoop.group.domain.modelo;

import com.example.SplitLoop.group.domain.entity.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupSummary {

    private Group group;

    private Integer members;

    private Integer activeRecurringExpenses;

    private Integer pendingOccurrences;

    private BigDecimal totalExpenses;

    private BigDecimal pendingAmount;

    private LocalDate nextDueDate;
}