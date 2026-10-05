package com.example.SplitLoop.payment.application.dto.query;

import com.example.SplitLoop.payment.domain.model.PaymentType;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record GetPaymentsQuery(
        UUID groupId,
        UUID occurrenceId,
        UUID userId,
        PaymentType type,
        LocalDate from,
        LocalDate to
) {}