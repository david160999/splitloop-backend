package com.example.SplitLoop.payment.controller.response;

import com.example.SplitLoop.payment.domain.entity.PaymentType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private UUID id;

    private UUID occurrenceId;

    private UUID splitId;

    private UUID fromUserId;

    private UUID toUserId;

    private BigDecimal amount;

    private PaymentType type;

    private String note;

    private LocalDateTime paidAt;

    private UUID createdBy;
}