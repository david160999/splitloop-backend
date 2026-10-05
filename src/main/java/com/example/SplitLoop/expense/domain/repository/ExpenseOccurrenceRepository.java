package com.example.SplitLoop.expense.domain.repository;

import com.example.SplitLoop.expense.domain.model.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceCriteria;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseOccurrenceRepository {

    ExpenseOccurrence save(ExpenseOccurrence occurrence);

    Optional<ExpenseOccurrence> findById(UUID id);

    List<ExpenseOccurrence> findByGroupId(UUID groupId);

    boolean existsByGroupIdAndStatusNot(UUID groupId, ExpenseOccurrenceStatus status);

    List<ExpenseOccurrence> findByRecurringExpenseId(UUID recurringExpenseId);

    List<ExpenseOccurrence> findFutureOccurrences(UUID recurringExpenseId, LocalDate today);

    Optional<ExpenseOccurrence> findTopByRecurringExpenseIdOrderByDueDateDesc(UUID recurringExpenseId);

    int countByGroupIdAndStatus(UUID groupId, ExpenseOccurrenceStatus status);

    BigDecimal sumAmountByGroupId(UUID groupId);

    Optional<LocalDate> findNextDueDateByGroupIdAndStatus(UUID groupId, ExpenseOccurrenceStatus status);

    List<ExpenseOccurrence> findAll(ExpenseOccurrenceCriteria criteria);

    void saveAll(List<ExpenseOccurrence> updatedOccurrences);
}