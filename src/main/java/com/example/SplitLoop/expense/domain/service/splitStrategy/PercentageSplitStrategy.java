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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class PercentageSplitStrategy implements SplitStrategy {

    @Override
    public List<ExpenseOccurrenceSplit> calculate(
            ExpenseOccurrence occurrence,
            List<RecurringExpenseParticipant> participants) {

        BigDecimal totalAmount = occurrence.amount().amount();

        List<ExpenseOccurrenceSplit> splits = new ArrayList<>();

        BigDecimal accumulated = BigDecimal.ZERO;

        for (int i = 0; i < participants.size(); i++) {

            RecurringExpenseParticipant participant = participants.get(i);

            BigDecimal amount;

            if (i == participants.size() - 1) {

                amount = totalAmount.subtract(accumulated);

            } else {

                amount = totalAmount
                        .multiply(participant.value())
                        .divide(
                                BigDecimal.valueOf(100),
                                2,
                                RoundingMode.HALF_UP);

                accumulated = accumulated.add(amount);
            }

            boolean paidBy = participant.user().equals(occurrence.paidBy());

            splits.add(
                    ExpenseOccurrenceSplit.builder()
                            .occurrence(occurrence)
                            .user(participant.user())
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
