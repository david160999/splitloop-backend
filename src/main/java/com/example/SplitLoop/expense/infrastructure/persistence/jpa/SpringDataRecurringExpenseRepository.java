package com.example.SplitLoop.expense.infrastructure.persistence.jpa;

import com.example.SplitLoop.expense.domain.model.RecurringExpenseStatus;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.RecurringExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SpringDataRecurringExpenseRepository extends JpaRepository<RecurringExpenseEntity, UUID> {

    List<RecurringExpenseEntity> findByGroupId(UUID groupId);

    List<RecurringExpenseEntity> findByGroupIdAndStatus(UUID groupId, RecurringExpenseStatus status);

    @Query("""
        SELECT COUNT(r) > 0
        FROM RecurringExpenseEntity r
        WHERE r.group.id = :groupId
          AND r.status = :status
    """)
    boolean existsByGroupIdAndStatus(@Param("groupId") UUID groupId, @Param("status") RecurringExpenseStatus status);

    int countByGroupIdAndStatus(UUID groupId, RecurringExpenseStatus status);

    List<RecurringExpenseEntity> findByStatus(RecurringExpenseStatus status);

    List<RecurringExpenseEntity> findByStatusAndStartDateLessThanEqual(RecurringExpenseStatus status, LocalDate date);
}
