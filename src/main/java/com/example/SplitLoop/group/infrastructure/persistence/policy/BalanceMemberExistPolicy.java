package com.example.SplitLoop.group.infrastructure.persistence.policy;

import com.example.SplitLoop.balance.domain.exception.MemberHasPendingDebtsException;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceSplitRepository;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.group.infrastructure.persistence.entity.GroupEntity;
import com.example.SplitLoop.group.domain.policy.MemberExitPolicy;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.user.infrastructure.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceMemberExistPolicy implements MemberExitPolicy {

    private final ExpenseOccurrenceSplitRepository splitRepository;

    @Override
    public void validateCanExist(Group group, User user) {

        boolean hasDebt = splitRepository.existsPendingDebt(
                group.id(),
                user.id());

        if (hasDebt) {
            throw new MemberHasPendingDebtsException();
        }
    }
}