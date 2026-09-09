package com.example.SplitLoop.balance.infrastruture;

import com.example.SplitLoop.balance.exception.MemberHasPendingDebtsException;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceSplitRepository;
import com.example.SplitLoop.group.domain.entity.Group;
import com.example.SplitLoop.group.domain.policy.MemberExitPolicy;
import com.example.SplitLoop.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceMemberExistPolicy  implements MemberExitPolicy {

    private final ExpenseOccurrenceSplitRepository splitRepository;

    @Override
    public void validateCanExist(Group group, User user) {

        boolean hasDebt = splitRepository.existsPendingDebt(
                        group.getId(),
                        user.getId());

        if (hasDebt) {
            throw new MemberHasPendingDebtsException();
        }
    }
}