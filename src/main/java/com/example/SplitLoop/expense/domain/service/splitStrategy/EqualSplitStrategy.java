package com.example.SplitLoop.expense.domain.service.splitStrategy;

import com.example.SplitLoop.expense.domain.model.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.model.RecurringExpenseParticipant;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceSplitEntity;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceSplitStatus;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.RecurringExpenseParticipantEntity;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.user.infrastructure.persistence.entity.UserEntity;
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

        BigDecimal total = occurrence.amount().amount();

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

            User participantUser = participants.get(i).user();

            boolean paidBy = participantUser.equals(occurrence.paidBy());

            splits.add(
                    ExpenseOccurrenceSplit.builder()
                            .occurrence(occurrence)
                            .user(participants.get(i).user())
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
