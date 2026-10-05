package com.example.SplitLoop.group.infrastructure.persistence.adapter;

import com.example.SplitLoop.expense.domain.model.RecurringExpenseStatus;
import com.example.SplitLoop.expense.infrastructure.persistence.jpa.SpringDataRecurringExpenseRepository;
import com.example.SplitLoop.group.domain.port.RecurringExpenseClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LocalRecurringExpenseAdapter implements RecurringExpenseClientPort {

    private final SpringDataRecurringExpenseRepository recurringExpenseRepository;

    @Override
    public int getActiveCountByGroupId(UUID groupId) {
        return recurringExpenseRepository.countByGroupIdAndStatus(groupId, RecurringExpenseStatus.ACTIVE);
    }
}