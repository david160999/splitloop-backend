package com.example.SplitLoop.balance.mapper;

import com.example.SplitLoop.balance.controller.response.BalanceResponse;
import com.example.SplitLoop.balance.controller.response.DebtResponse;
import com.example.SplitLoop.balance.domain.modelo.Balance;
import com.example.SplitLoop.balance.domain.modelo.Debt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BalanceMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "balance", source = "amount")
    BalanceResponse toResponse(Balance balance);

    @Mapping(target = "debtorId", source = "debtor.id")
    @Mapping(target = "debtorUsername", source = "debtor.username")
    @Mapping(target = "creditorId", source = "creditor.id")
    @Mapping(target = "creditorUsername", source = "creditor.username")
    DebtResponse toResponse(Debt debt);

    List<BalanceResponse> toBalanceResponses(List<Balance> balances);

    List<DebtResponse> toDebtResponses(List<Debt> debts);
}