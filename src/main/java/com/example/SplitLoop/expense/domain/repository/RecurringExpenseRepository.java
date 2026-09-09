package com.example.SplitLoop.expense.domain.repository;

import com.example.SplitLoop.expense.domain.entity.RecurringExpense;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseStatus;
import com.example.SplitLoop.group.domain.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RecurringExpenseRepository extends JpaRepository<RecurringExpense, UUID> {

    List<RecurringExpense> findByGroup(Group group);

    List<RecurringExpense> findByGroupAndStatus(
            Group group,
            RecurringExpenseStatus status
    );

    @Query("""
                SELECT COUNT(r) > 0
                FROM RecurringExpense r
                WHERE r.group.id = :groupId
                  AND r.status = :status
            """)
    boolean existsByGroupIdAndStatus(
            UUID groupId,
            RecurringExpenseStatus status
    );

    int countByGroupAndStatus(Group group, RecurringExpenseStatus recurringExpenseStatus);

    List<RecurringExpense> findByStatus(RecurringExpenseStatus recurringExpenseStatus);

    List<RecurringExpense> findByStatusAndStartDateLessThanEqual(
            RecurringExpenseStatus status,
            LocalDate date);
}