package com.example.SplitLoop.expense.infrastructure.persistence.jpa;

import com.example.SplitLoop.expense.infrastructure.persistence.entity.RecurringExpenseParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataRecurringExpenseParticipantRepository
        extends JpaRepository<RecurringExpenseParticipantEntity, UUID> {

    List<RecurringExpenseParticipantEntity> findByRecurringExpenseId(UUID recurringExpenseId);

    void deleteByRecurringExpenseId(UUID recurringExpenseId);
}
