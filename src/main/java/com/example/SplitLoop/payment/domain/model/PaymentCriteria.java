package com.example.SplitLoop.payment.domain.model;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record PaymentCriteria(
        UUID groupId,
        UUID occurrenceId,
        UUID userId,
        PaymentType type,
        LocalDate from,
        LocalDate to
) {}