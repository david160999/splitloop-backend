package com.example.SplitLoop.balance.infrastruture.presentation.rest.controller;

import com.example.SplitLoop.balance.application.usecase.query.GetBalancesUseCase;
import com.example.SplitLoop.balance.application.usecase.query.GetDebtsUseCase;
import com.example.SplitLoop.balance.application.dto.query.GetBalancesQueryFilter;
import com.example.SplitLoop.balance.application.dto.response.BalanceResponse;
import com.example.SplitLoop.balance.application.dto.response.DebtResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/balances")
@RequiredArgsConstructor
@Tag(name = "Balances")
public class BalanceController {

    private final GetBalancesUseCase getBalancesUseCase;
    private final GetDebtsUseCase getDebtsUseCase;

    @GetMapping
    @Operation(summary = "Get balances")
    public ResponseEntity<List<BalanceResponse>> getBalances(@Valid GetBalancesQueryFilter query) {

        return ResponseEntity.ok(getBalancesUseCase.execute(query));
    }

    @GetMapping("/debts")
    @Operation(summary = "Get debts")
    public ResponseEntity<List<DebtResponse>> getDebts(@Valid GetBalancesQueryFilter query) {

        return ResponseEntity.ok(getDebtsUseCase.execute(query));
    }
}
