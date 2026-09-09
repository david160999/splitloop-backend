package com.example.SplitLoop.balance.controller.query;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetBalancesQuery {

    @NotNull
    private UUID groupId;

    private LocalDate from;

    private LocalDate to;
}
