package com.example.SplitLoop.group.infrastructure.persistence.policy;


import com.example.SplitLoop.balance.domain.exception.GroupHasActiveExpensesException;
import com.example.SplitLoop.expense.domain.model.RecurringExpenseStatus;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.group.domain.repository.GroupRepository;
import com.example.SplitLoop.group.infrastructure.persistence.entity.GroupEntity;
import com.example.SplitLoop.group.domain.policy.GroupDeletionPolicy;
import com.example.SplitLoop.group.infrastructure.persistence.mapper.GroupPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceGroupDeletionPolicy implements GroupDeletionPolicy {

    private final RecurringExpenseRepository recurringExpenseRepository;

    @Override
    public void validateCanDelete(Group group) {
        boolean hasActiveExpenses = recurringExpenseRepository
                .existsByGroupIdAndStatus(group.id(), RecurringExpenseStatus.ACTIVE);

        if (hasActiveExpenses) {
            throw new GroupHasActiveExpensesException();
        }
    }
}

