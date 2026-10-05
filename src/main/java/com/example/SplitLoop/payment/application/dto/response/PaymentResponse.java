package com.example.SplitLoop.payment.application.dto.response;

import com.example.SplitLoop.payment.domain.model.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        UUID occurrenceId,
        UUID splitId,
        UUID fromUserId,
        UUID toUserId,
        BigDecimal amount,
        PaymentType type,
        String note,
        LocalDateTime paidAt,
        UUID createdBy
) {}