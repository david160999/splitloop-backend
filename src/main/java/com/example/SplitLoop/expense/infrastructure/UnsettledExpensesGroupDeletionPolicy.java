package com.example.SplitLoop.expense.infrastructure;

import com.example.SplitLoop.balance.exception.GroupHasUnsettledExpensesException;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceStatus;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.policy.GroupDeletionPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnsettledExpensesGroupDeletionPolicy implements GroupDeletionPolicy {

    private final ExpenseOccurrenceRepository expenseOccurrenceRepository;

    @Override
    public void validateCanDelete(Group group) {
        if (expenseOccurrenceRepository.existsByGroupIdAndStatusNot(
                group.getId(),
                ExpenseOccurrenceStatus.PAID)) {

            throw new GroupHasUnsettledExpensesException();
        }
    }
}
