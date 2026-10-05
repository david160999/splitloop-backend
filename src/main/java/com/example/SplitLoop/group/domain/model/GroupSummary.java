package com.example.SplitLoop.group.domain.model;

import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Builder(toBuilder = true)
public record GroupSummary(
        Group group,
        Integer members,
        Integer activeRecurringExpenses,
        Integer pendingOccurrences,
        BigDecimal totalExpenses,
        BigDecimal pendingAmount,
        LocalDate nextDueDate
) {
    public GroupSummary {
        Objects.requireNonNull(group, "El grupo no puede ser nulo");
    }
}