package com.example.SplitLoop.expense.controller.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantResponse {

    private UUID userId;

    private String username;

    private BigDecimal value;
}
