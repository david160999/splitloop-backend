package com.example.SplitLoop.balance.infrastruture;


import com.example.SplitLoop.balance.exception.GroupHasActiveExpensesException;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseStatus;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.policy.GroupDeletionPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceGroupDeletionPolicy implements GroupDeletionPolicy {

    private final RecurringExpenseRepository recurringExpenseRepository;

    @Override
    public void validateCanDelete(Group group) {

        if (recurringExpenseRepository.existsByGroupIdAndStatus(group.getId(), RecurringExpenseStatus.ACTIVE)) {
            throw new GroupHasActiveExpensesException();
        }
    }
}

