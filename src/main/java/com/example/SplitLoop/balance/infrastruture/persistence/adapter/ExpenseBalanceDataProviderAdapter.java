package com.example.SplitLoop.balance.infrastruture.persistence.adapter;

import com.example.SplitLoop.balance.application.dto.query.GetBalancesQueryFilter;
import com.example.SplitLoop.balance.domain.model.PendingExpenseData;
import com.example.SplitLoop.balance.domain.port.BalanceDataProviderPort;
import com.example.SplitLoop.balance.infrastruture.persistence.mapper.BalancePersistenceMapper;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceSplitEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.jpa.SpringDataExpenseOccurrenceSplitRepository;
import com.example.SplitLoop.expense.infrastructure.presentation.rest.specification.ExpenseOccurrenceSplitSpecifications;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExpenseBalanceDataProviderAdapter implements BalanceDataProviderPort {

    private final GroupRepository groupRepository;
    private final SpringDataExpenseOccurrenceSplitRepository splitJpaRepository;
    private final BalancePersistenceMapper balancePersistenceMapper;

    @Override
    public boolean existsGroupById(UUID groupId) {
        return groupRepository.findById(groupId).isPresent();
    }

    @Override
    public List<PendingExpenseData> findPendingExpenses(GetBalancesQueryFilter filter) {
        Specification<ExpenseOccurrenceSplitEntity> spec = Specification.where(ExpenseOccurrenceSplitSpecifications.hasGroup(filter.groupId()));

        if (filter.from() != null) {
            spec = spec.and(ExpenseOccurrenceSplitSpecifications.dueDateAfter(filter.from()));
        }

        if (filter.to() != null) {
            spec = spec.and(ExpenseOccurrenceSplitSpecifications.dueDateBefore(filter.to()));
        }

        return splitJpaRepository.findAll(spec).stream()
                .map(balancePersistenceMapper::toPendingExpenseData)
                .toList();
    }
}