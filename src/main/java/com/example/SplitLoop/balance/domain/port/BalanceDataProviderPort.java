package com.example.SplitLoop.balance.domain.port;

import com.example.SplitLoop.balance.application.dto.query.GetBalancesQueryFilter;
import com.example.SplitLoop.balance.domain.model.PendingExpenseData;

import java.util.List;
import java.util.UUID;

public interface BalanceDataProviderPort {

    boolean existsGroupById(UUID groupId);
    List<PendingExpenseData> findPendingExpenses(GetBalancesQueryFilter filter);
}
