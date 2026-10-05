package com.example.SplitLoop.balance.application.usecase.query;

import com.example.SplitLoop.balance.application.dto.query.GetBalancesQueryFilter;
import com.example.SplitLoop.balance.application.dto.response.BalanceResponse;
import com.example.SplitLoop.balance.domain.model.PendingExpenseData;
import com.example.SplitLoop.balance.domain.port.BalanceDataProviderPort;
import com.example.SplitLoop.balance.domain.service.BalanceService;
import com.example.SplitLoop.balance.application.mapper.BalanceDtoMapper;
import com.example.SplitLoop.group.domain.exception.GroupNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetBalancesUseCase {

    private final BalanceDataProviderPort balanceDataProviderPort;
    private final BalanceService balanceService;
    private final BalanceDtoMapper mapper;

    @Transactional(readOnly = true)
    public List<BalanceResponse> execute(GetBalancesQueryFilter query) {

        // 1. Validar la existencia del grupo a través del puerto
        if (!balanceDataProviderPort.existsGroupById(query.groupId())) {
            throw new GroupNotFoundException(query.groupId());
        }

        // 2. Mapear la consulta al filtro del puerto
        GetBalancesQueryFilter filter = new GetBalancesQueryFilter(
                query.groupId(),
                query.from(),
                query.to()
        );

        // 3. Obtener los datos neutros
        List<PendingExpenseData> expenses = balanceDataProviderPort.findPendingExpenses(filter);

        // 4. Calcular los saldos utilizando el servicio de dominio puro
        return mapper.toBalanceResponses(balanceService.calculateBalances(expenses));
    }
}