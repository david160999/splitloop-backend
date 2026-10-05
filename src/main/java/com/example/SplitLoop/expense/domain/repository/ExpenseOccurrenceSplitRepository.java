package com.example.SplitLoop.expense.domain.repository;

import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceSplit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseOccurrenceSplitRepository {

    ExpenseOccurrenceSplit save(ExpenseOccurrenceSplit split);

    List<ExpenseOccurrenceSplit> saveAll(List<ExpenseOccurrenceSplit> splits);

    Optional<ExpenseOccurrenceSplit> findById(UUID id);

    List<ExpenseOccurrenceSplit> findByOccurrenceId(UUID occurrenceId);

    void deleteByOccurrenceId(UUID occurrenceId);

    boolean existsPendingDebt(UUID groupId, UUID userId);

    List<ExpenseOccurrenceSplit> findByOccurrenceGroupId(UUID groupId);

    void cancelSplitsForOccurrence(UUID occurrenceId);

    List<ExpenseOccurrenceSplit> findByOccurrenceIdIn(List<UUID> occurrenceIds);
}
