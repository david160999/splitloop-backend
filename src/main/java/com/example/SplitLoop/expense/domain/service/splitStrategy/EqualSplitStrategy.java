package com.example.SplitLoop.expense.domain.service.splitStrategy;

import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplitStatus;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseParticipant;
import com.example.SplitLoop.user.domain.entity.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class EqualSplitStrategy implements SplitStrategy {

    @Override
    public List<ExpenseOccurrenceSplit> calculate(
            ExpenseOccurrence occurrence,
            List<RecurringExpenseParticipant> participants) {

        BigDecimal total = occurrence.getAmount();

        BigDecimal amount = total.divide(
                BigDecimal.valueOf(participants.size()),
                2,
                RoundingMode.DOWN);

        BigDecimal accumulated = BigDecimal.ZERO;

        List<ExpenseOccurrenceSplit> splits = new ArrayList<>();

        for (int i = 0; i < participants.size(); i++) {

            BigDecimal owed;

            if (i == participants.size() - 1) {
                owed = total.subtract(accumulated);
            } else {
                owed = amount;
                accumulated = accumulated.add(amount);
            }

            User participantUser = participants.get(i).getUser();

            boolean paidBy = participantUser.equals(occurrence.getPaidBy());

            splits.add(
                    ExpenseOccurrenceSplit.builder()
                            .occurrence(occurrence)
                            .user(participants.get(i).getUser())
                            .amountOwed(owed)
                            .amountPaid(paidBy ? owed : BigDecimal.ZERO)
                            .status(paidBy
                                    ? ExpenseOccurrenceSplitStatus.PAID
                                    : ExpenseOccurrenceSplitStatus.PENDING)
                            .build()
            );
        }

        return splits;
    }
}
