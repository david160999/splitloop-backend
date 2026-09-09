package com.example.SplitLoop.payment.controller.query;

import com.example.SplitLoop.payment.domain.entity.PaymentType;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetPaymentsQuery {

    private UUID groupId;

    private UUID occurrenceId;

    private UUID userId;

    private PaymentType type;

    private LocalDate from;

    private LocalDate to;
}