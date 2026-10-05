package com.example.SplitLoop.expense.infrastructure.persistence.jpa;

import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceStatus;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataExpenseOccurrenceRepository extends JpaRepository<ExpenseOccurrenceEntity, UUID>, JpaSpecificationExecutor<ExpenseOccurrenceEntity> {

    List<ExpenseOccurrenceEntity> findByGroupId(UUID groupId);

    boolean existsByGroupIdAndStatusNot(UUID groupId, ExpenseOccurrenceStatus status);

    List<ExpenseOccurrenceEntity> findByRecurringExpenseId(UUID recurringExpenseId);

    @Query("""
        SELECT o
        FROM ExpenseOccurrenceEntity o
        WHERE o.recurringExpense.id = :recurringExpenseId
          AND o.dueDate >= :today
          AND o.status <> :cancelledStatus
        """)
    List<ExpenseOccurrenceEntity> findFutureOccurrences(
            @Param("recurringExpenseId") UUID recurringExpenseId,
            @Param("today") LocalDate today,
            @Param("cancelledStatus") ExpenseOccurrenceStatus cancelledStatus
    );

    Optional<ExpenseOccurrenceEntity> findTopByRecurringExpenseIdOrderByDueDateDesc(UUID recurringExpenseId);

    int countByGroupIdAndStatus(UUID groupId, ExpenseOccurrenceStatus status);

    @Query("""
        SELECT COALESCE(SUM(o.amount), 0)
        FROM ExpenseOccurrenceEntity o
        WHERE o.group.id = :groupId
        """)
    BigDecimal sumAmountByGroupId(@Param("groupId") UUID groupId);

    @Query("""
        SELECT MIN(o.dueDate)
        FROM ExpenseOccurrenceEntity o
        WHERE o.group.id = :groupId
          AND o.status = :status
        """)
    Optional<LocalDate> findNextDueDate(
            @Param("groupId") UUID groupId,
            @Param("status") ExpenseOccurrenceStatus status
    );
}
