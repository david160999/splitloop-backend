package com.example.SplitLoop.balance.infrastruture.persistence.mapper;

import com.example.SplitLoop.balance.domain.model.PendingExpenseData;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceSplitEntity;
import com.example.SplitLoop.user.infrastructure.persistence.mapper.UserPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalancePersistenceMapper {

    private final UserPersistenceMapper userMapper;

    public PendingExpenseData toPendingExpenseData(ExpenseOccurrenceSplitEntity entity) {
        if (entity == null) {
            return null;
        }

        return new PendingExpenseData(
                userMapper.toDomain(entity.getUser()),
                userMapper.toDomain(entity.getOccurrence().getPaidBy()),
                entity.getAmountOwed(),
                entity.getAmountPaid()
        );
    }
}