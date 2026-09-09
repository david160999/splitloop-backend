package com.example.SplitLoop.expense.domain.service.splitStrategy;

import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplitStatus;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseParticipant;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class PercentageSplitStrategy implements SplitStrategy {

    @Override
    public List<ExpenseOccurrenceSplit> calculate(
            ExpenseOccurrence occurrence,
            List<RecurringExpenseParticipant> participants) {

        BigDecimal totalAmount = occurrence.getAmount();

        List<ExpenseOccurrenceSplit> splits = new ArrayList<>();

        BigDecimal accumulated = BigDecimal.ZERO;

        for (int i = 0; i < participants.size(); i++) {

            RecurringExpenseParticipant participant = participants.get(i);

            BigDecimal amount;

            if (i == participants.size() - 1) {

                amount = totalAmount.subtract(accumulated);

            } else {

                amount = totalAmount
                        .multiply(participant.getValue())
                        .divide(
                                BigDecimal.valueOf(100),
                                2,
                                RoundingMode.HALF_UP);

                accumulated = accumulated.add(amount);
            }

            boolean paidBy = participant.getUser().equals(occurrence.getPaidBy());

            splits.add(
                    ExpenseOccurrenceSplit.builder()
                            .occurrence(occurrence)
                            .user(participant.getUser())
                            .amountOwed(amount)
                            .amountPaid(
                                    paidBy
                                            ? amount
                                            : BigDecimal.ZERO)
                            .status(
                                    paidBy
                                            ? ExpenseOccurrenceSplitStatus.PAID
                                            : ExpenseOccurrenceSplitStatus.PENDING)
                            .build()
            );
        }

        return splits;
    }
}
