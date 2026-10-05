package com.example.SplitLoop.expense.infrastructure.persistence.jpa;

import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceSplitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SpringDataExpenseOccurrenceSplitRepository
        extends JpaRepository<ExpenseOccurrenceSplitEntity, UUID>, JpaSpecificationExecutor<ExpenseOccurrenceSplitEntity> {

    List<ExpenseOccurrenceSplitEntity> findByOccurrenceId(UUID occurrenceId);

    void deleteByOccurrenceId(UUID occurrenceId);

    @Query("""
                SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END
                FROM ExpenseOccurrenceSplitEntity s
                WHERE s.user.id = :userId
                  AND s.occurrence.group.id = :groupId
                  AND s.amountPaid < s.amountOwed
            """)
    boolean existsPendingDebt(@Param("groupId") UUID groupId, @Param("userId") UUID userId);

    List<ExpenseOccurrenceSplitEntity> findByOccurrenceGroupId(UUID groupId);

    @Modifying
    @Query("UPDATE ExpenseOccurrenceSplitEntity s SET s.status = 'CANCELLED' WHERE s.occurrence.id = :occurrenceId")
    int cancelSplitsByOccurrenceId(@Param("occurrenceId") UUID occurrenceId);

    List<ExpenseOccurrenceSplitEntity> findByOccurrenceIdIn(List<UUID> occurrenceIds);
}