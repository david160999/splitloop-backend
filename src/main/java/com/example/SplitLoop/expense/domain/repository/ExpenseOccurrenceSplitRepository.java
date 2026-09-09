package com.example.SplitLoop.expense.domain.repository;

import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ExpenseOccurrenceSplitRepository extends JpaRepository<ExpenseOccurrenceSplit, UUID>, JpaSpecificationExecutor<ExpenseOccurrenceSplit> {

    List<RecurringExpenseParticipant> findByUserId(UUID userId);

    List<ExpenseOccurrenceSplit> findByOccurrence(ExpenseOccurrence occurrence);

    void deleteByOccurrence(ExpenseOccurrence occurrence);

    @Query("""
        SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END
        FROM ExpenseOccurrenceSplit s
        WHERE s.user.id = :userId
          AND s.occurrence.recurringExpense.group.id = :groupId
          AND s.amountPaid < s.amountOwed
    """)
    boolean existsPendingDebt(UUID groupId, UUID userId);

    List<ExpenseOccurrenceSplit> findByOccurrenceGroupId(UUID groupId);
}
