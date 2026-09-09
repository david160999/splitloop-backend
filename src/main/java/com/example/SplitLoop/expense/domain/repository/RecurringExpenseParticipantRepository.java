package com.example.SplitLoop.expense.domain.repository;

import com.example.SplitLoop.expense.domain.entity.RecurringExpense;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RecurringExpenseParticipantRepository extends JpaRepository<RecurringExpenseParticipant, UUID> {
    List<RecurringExpenseParticipant> findByRecurringExpense(RecurringExpense recurringExpense);

    void deleteByRecurringExpense(RecurringExpense recurringExpense);
}