package com.example.SplitLoop.expense.domain.service.splitStrategy;

import com.example.SplitLoop.expense.domain.model.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.model.RecurringExpenseParticipant;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceSplitEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.RecurringExpenseParticipantEntity;

import java.util.List;

public interface SplitStrategy {

    List<ExpenseOccurrenceSplit> calculate(
            ExpenseOccurrence occurrence,
            List<RecurringExpenseParticipant> participants);

}
