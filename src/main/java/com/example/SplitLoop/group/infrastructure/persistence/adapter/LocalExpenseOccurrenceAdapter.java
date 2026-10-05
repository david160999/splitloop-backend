package com.example.SplitLoop.group.infrastructure.persistence.adapter;

import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceStatus;
import com.example.SplitLoop.expense.infrastructure.persistence.jpa.SpringDataExpenseOccurrenceRepository;
import com.example.SplitLoop.group.domain.model.OccurrenceSummaryData;
import com.example.SplitLoop.group.domain.port.ExpenseOccurrenceClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LocalExpenseOccurrenceAdapter implements ExpenseOccurrenceClientPort {

    private final SpringDataExpenseOccurrenceRepository occurrenceRepository;

    @Override
    public OccurrenceSummaryData getOccurrenceSummaryByGroupId(UUID groupId) {
        int pendingCount = occurrenceRepository.countByGroupIdAndStatus(groupId, ExpenseOccurrenceStatus.PENDING);
        BigDecimal totalAmount = occurrenceRepository.sumAmountByGroupId(groupId);
        LocalDate nextDueDate = occurrenceRepository.findNextDueDate(groupId, ExpenseOccurrenceStatus.PENDING)
                .orElse(null);

        return new OccurrenceSummaryData(
                pendingCount,
                totalAmount != null ? totalAmount : BigDecimal.ZERO,
                nextDueDate
        );
    }
}
