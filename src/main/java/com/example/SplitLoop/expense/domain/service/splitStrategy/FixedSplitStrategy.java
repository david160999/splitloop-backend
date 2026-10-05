package com.example.SplitLoop.expense.domain.service.splitStrategy;

import com.example.SplitLoop.expense.domain.model.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.model.RecurringExpenseParticipant;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceSplitEntity;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceSplitStatus;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.RecurringExpenseParticipantEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class FixedSplitStrategy implements SplitStrategy {

    @Override
    public List<ExpenseOccurrenceSplit> calculate(
            ExpenseOccurrence occurrence,
            List<RecurringExpenseParticipant> participants) {

        return participants.stream()
                .map(participant -> {

                    boolean paidBy = participant.user().equals(occurrence.paidBy());

                    return ExpenseOccurrenceSplit.builder()
                            .occurrence(occurrence)
                            .user(participant.user())
                            .amountOwed(participant.value())
                            .amountPaid(
                                    paidBy
                                            ? participant.value()
                                            : BigDecimal.ZERO)
                            .status(
                                    paidBy
                                            ? ExpenseOccurrenceSplitStatus.PAID
                                            : ExpenseOccurrenceSplitStatus.PENDING)
                            .build();
                })
                .toList();
    }
}
