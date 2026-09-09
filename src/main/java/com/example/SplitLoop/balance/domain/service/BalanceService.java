package com.example.SplitLoop.balance.domain.service;

import com.example.SplitLoop.balance.domain.modelo.Balance;
import com.example.SplitLoop.balance.domain.modelo.Debt;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplit;

import java.util.List;

public interface BalanceService {

    List<Balance> calculateBalances(List<ExpenseOccurrenceSplit> splits);

    List<Debt> calculateDebts(List<ExpenseOccurrenceSplit> splits);
}
