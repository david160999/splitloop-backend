package com.example.SplitLoop.expense.domain.repository;

import com.example.SplitLoop.expense.domain.model.RecurringExpense;
import com.example.SplitLoop.expense.domain.model.RecurringExpenseStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecurringExpenseRepository {

    RecurringExpense save(RecurringExpense recurringExpense);

    Optional<RecurringExpense> findById(UUID id);

    List<RecurringExpense> findByGroupId(UUID groupId);

    List<RecurringExpense> findByGroupIdAndStatus(UUID groupId, RecurringExpenseStatus status);

    boolean existsByGroupIdAndStatus(UUID groupId, RecurringExpenseStatus status);

    int countByGroupIdAndStatus(UUID groupId, RecurringExpenseStatus status);

    List<RecurringExpense> findByStatus(RecurringExpenseStatus status);

    List<RecurringExpense> findByStatusAndStartDateLessThanEqual(RecurringExpenseStatus status, LocalDate date);

    void deleteById(UUID id);
}