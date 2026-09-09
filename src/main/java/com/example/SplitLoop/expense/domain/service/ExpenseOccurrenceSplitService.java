package com.example.SplitLoop.expense.domain.service;

import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;

public interface ExpenseOccurrenceSplitService {
    void recalculateParticipants(ExpenseOccurrence occurrence);

    void updateOccurrenceStatus(ExpenseOccurrence occurrence);
}
