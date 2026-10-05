package com.example.SplitLoop.expense.application.dto.response;


import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;

@Builder
public record ParticipantResponse(
        UUID userId,
        String username,
        BigDecimal value
) {}
