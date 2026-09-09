package com.example.SplitLoop.expense.domain.service.splitStrategy;

import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseParticipant;

import java.util.List;

public interface SplitStrategy {

    List<ExpenseOccurrenceSplit> calculate(
            ExpenseOccurrence occurrence,
            List<RecurringExpenseParticipant> participants);

}
