package com.example.SplitLoop.expense.domain.repository;

import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceStatus;
import com.example.SplitLoop.expense.domain.entity.RecurringExpense;
import com.example.SplitLoop.group.domain.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseOccurrenceRepository extends JpaRepository<ExpenseOccurrence, UUID>, JpaSpecificationExecutor<ExpenseOccurrence> {
    List<ExpenseOccurrence> findByGroupId(UUID groupId);

    boolean existsByGroupIdAndStatusNot(
            UUID groupId,
            ExpenseOccurrenceStatus status
    );

    List<ExpenseOccurrence> findByRecurringExpense(RecurringExpense recurringExpense);

    @Query("""
             SELECT o
             FROM ExpenseOccurrence o
             WHERE o.recurringExpense = :recurringExpense
             AND o.dueDate >= :today
             AND o.status <> com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceStatus.CANCELLED
            """)
    List<ExpenseOccurrence> findFutureOccurrences(
            @Param("recurringExpense") RecurringExpense recurringExpense,
            @Param("today") LocalDate today);

    Optional<ExpenseOccurrence> findTopByRecurringExpenseOrderByDueDateDesc(RecurringExpense recurringExpense);

    int countByGroupAndStatus(Group group, ExpenseOccurrenceStatus expenseOccurrenceStatus);

    @Query("""
                SELECT COALESCE(SUM(o.amount), 0)
                FROM ExpenseOccurrence o
                WHERE o.group = :group
            """)
    BigDecimal sumAmountByGroup(@Param("group") Group group);

    @Query("""
                SELECT MIN(o.dueDate)
                FROM ExpenseOccurrence o
                WHERE o.group = :group
                  AND o.status = :status
            """)
    LocalDate findNextDueDate(
            @Param("group") Group group,
            @Param("status") ExpenseOccurrenceStatus status);
}