package com.example.SplitLoop.expense.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantRequest {

    @NotNull
    private UUID userId;

    private BigDecimal splitValue;
}