package com.example.SplitLoop.balance.application.dto.query;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record GetBalancesQueryFilter(
        @NotNull UUID groupId,
        LocalDate from,
        LocalDate to
) {}
