package com.example.SplitLoop.balance.application.query;

import com.example.SplitLoop.balance.controller.query.GetBalancesQuery;
import com.example.SplitLoop.balance.controller.response.BalanceResponse;
import com.example.SplitLoop.balance.domain.service.BalanceService;
import com.example.SplitLoop.balance.mapper.BalanceMapper;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceSplitRepository;
import com.example.SplitLoop.expense.infrastructure.specification.ExpenseOccurrenceSplitSpecifications;
import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.exception.GroupNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetBalancesUseCase {

    private final GroupRepository groupRepository;
    private final BalanceService balanceService;
    private final BalanceMapper mapper;
    private final ExpenseOccurrenceSplitRepository splitRepository;

    @Transactional(readOnly = true)
    public List<BalanceResponse> execute(GetBalancesQuery query) {

        Group group = groupRepository.findById(query.getGroupId())
                .orElseThrow(() -> new GroupNotFoundException(query.getGroupId()));

        Specification<ExpenseOccurrenceSplit> spec = Specification.where(ExpenseOccurrenceSplitSpecifications.hasGroup(group.getId()));

        if (query.getFrom() != null) {
            spec = spec.and(ExpenseOccurrenceSplitSpecifications.dueDateAfter(query.getFrom()));
        }

        if (query.getTo() != null) {
            spec = spec.and(ExpenseOccurrenceSplitSpecifications.dueDateBefore(query.getTo()));
        }

        List<ExpenseOccurrenceSplit> splits = splitRepository.findAll(spec);

        return mapper.toBalanceResponses(balanceService.calculateBalances(splits));
    }
}