package com.example.SplitLoop.group.infrastructure.persistence.policy;

import com.example.SplitLoop.balance.domain.exception.GroupHasUnsettledExpensesException;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceStatus;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.group.domain.model.Group;
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
                group.id(),
                ExpenseOccurrenceStatus.PAID)) {

            throw new GroupHasUnsettledExpensesException();
        }
    }
}
