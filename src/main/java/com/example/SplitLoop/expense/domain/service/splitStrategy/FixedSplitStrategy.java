package com.example.SplitLoop.expense.domain.service.splitStrategy;

import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplitStatus;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseParticipant;
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

                    boolean paidBy = participant.getUser().equals(occurrence.getPaidBy());

                    return ExpenseOccurrenceSplit.builder()
                            .occurrence(occurrence)
                            .user(participant.getUser())
                            .amountOwed(participant.getValue())
                            .amountPaid(
                                    paidBy
                                            ? participant.getValue()
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
