package com.example.SplitLoop.balance.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DebtResponse {

    private UUID debtorId;

    private String debtorUsername;

    private UUID creditorId;

    private String creditorUsername;

    private BigDecimal amount;
}
