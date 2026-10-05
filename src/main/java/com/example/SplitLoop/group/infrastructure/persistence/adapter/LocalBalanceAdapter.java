package com.example.SplitLoop.group.infrastructure.persistence.adapter;

import com.example.SplitLoop.balance.application.dto.query.GetBalancesQueryFilter;
import com.example.SplitLoop.balance.domain.port.BalanceDataProviderPort;
import com.example.SplitLoop.balance.domain.service.BalanceService;
import com.example.SplitLoop.group.domain.port.BalanceClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LocalBalanceAdapter implements BalanceClientPort {

    private final BalanceDataProviderPort balanceDataProviderPort;
    private final BalanceService balanceService;

    @Override
    public BigDecimal getPendingAmountByGroupId(UUID groupId, LocalDate from, LocalDate to) {
        // El adaptador de Group instancia el DTO de Balance AQUÍ, nunca en el UseCase
        var filter = new GetBalancesQueryFilter(groupId, from, to);
        var expenses = balanceDataProviderPort.findPendingExpenses(filter);

        return balanceService.calculateBalances(expenses).stream()
                .filter(b -> b.amount().compareTo(BigDecimal.ZERO) < 0)
                .map(b -> b.amount().abs())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
