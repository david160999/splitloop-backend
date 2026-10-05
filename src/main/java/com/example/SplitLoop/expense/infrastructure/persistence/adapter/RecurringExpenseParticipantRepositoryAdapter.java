package com.example.SplitLoop.expense.infrastructure.persistence.adapter;

import com.example.SplitLoop.expense.domain.model.RecurringExpenseParticipant;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseParticipantRepository;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.RecurringExpenseParticipantEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.jpa.SpringDataRecurringExpenseParticipantRepository;
import com.example.SplitLoop.expense.infrastructure.persistence.mapper.ExpensePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RecurringExpenseParticipantRepositoryAdapter implements RecurringExpenseParticipantRepository {

    private final SpringDataRecurringExpenseParticipantRepository jpaRepository;
    private final ExpensePersistenceMapper expenseMapper;

    @Override
    public RecurringExpenseParticipant save(RecurringExpenseParticipant participant) {
        RecurringExpenseParticipantEntity entity = expenseMapper.toEntity(participant);
        RecurringExpenseParticipantEntity savedEntity = jpaRepository.save(entity);
        return expenseMapper.toDomain(savedEntity);
    }

    @Override
    public List<RecurringExpenseParticipant> saveAll(List<RecurringExpenseParticipant> participants) {
        List<RecurringExpenseParticipantEntity> entities = participants.stream()
                .map(expenseMapper::toEntity)
                .toList();
        return jpaRepository.saveAll(entities).stream()
                .map(expenseMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<RecurringExpenseParticipant> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(expenseMapper::toDomain);
    }

    @Override
    public List<RecurringExpenseParticipant> findByRecurringExpenseId(UUID recurringExpenseId) {
        return jpaRepository.findByRecurringExpenseId(recurringExpenseId)
                .stream()
                .map(expenseMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteByRecurringExpenseId(UUID recurringExpenseId) {
        jpaRepository.deleteByRecurringExpenseId(recurringExpenseId);
    }
}
