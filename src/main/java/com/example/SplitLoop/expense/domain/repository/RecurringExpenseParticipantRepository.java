package com.example.SplitLoop.expense.domain.repository;

import com.example.SplitLoop.expense.domain.model.RecurringExpenseParticipant;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface RecurringExpenseParticipantRepository {

    RecurringExpenseParticipant save(RecurringExpenseParticipant participant);

    List<RecurringExpenseParticipant> saveAll(List<RecurringExpenseParticipant> participants);

    Optional<RecurringExpenseParticipant> findById(UUID id);

    List<RecurringExpenseParticipant> findByRecurringExpenseId(UUID recurringExpenseId);

    void deleteByRecurringExpenseId(UUID recurringExpenseId);
}