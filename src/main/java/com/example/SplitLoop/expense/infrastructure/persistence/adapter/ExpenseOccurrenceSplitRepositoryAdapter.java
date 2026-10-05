package com.example.SplitLoop.expense.infrastructure.persistence.adapter;

import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceSplitRepository;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceSplitEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.jpa.SpringDataExpenseOccurrenceSplitRepository;
import com.example.SplitLoop.expense.infrastructure.persistence.mapper.ExpensePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ExpenseOccurrenceSplitRepositoryAdapter implements ExpenseOccurrenceSplitRepository {

    private final SpringDataExpenseOccurrenceSplitRepository jpaRepository;
    private final ExpensePersistenceMapper expenseMapper;

    @Override
    public ExpenseOccurrenceSplit save(ExpenseOccurrenceSplit split) {
        ExpenseOccurrenceSplitEntity entity = expenseMapper.toEntity(split);
        ExpenseOccurrenceSplitEntity savedEntity = jpaRepository.save(entity);
        return expenseMapper.toDomain(savedEntity);
    }

    @Override
    public List<ExpenseOccurrenceSplit> saveAll(List<ExpenseOccurrenceSplit> splits) {
        List<ExpenseOccurrenceSplitEntity> entities = splits.stream()
                .map(expenseMapper::toEntity)
                .toList();
        return jpaRepository.saveAll(entities).stream()
                .map(expenseMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<ExpenseOccurrenceSplit> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(expenseMapper::toDomain);
    }

    @Override
    public List<ExpenseOccurrenceSplit> findByOccurrenceId(UUID occurrenceId) {
        return jpaRepository.findByOccurrenceId(occurrenceId)
                .stream()
                .map(expenseMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteByOccurrenceId(UUID occurrenceId) {
        jpaRepository.deleteByOccurrenceId(occurrenceId);
    }

    @Override
    public boolean existsPendingDebt(UUID groupId, UUID userId) {
        return jpaRepository.existsPendingDebt(groupId, userId);
    }

    @Override
    public List<ExpenseOccurrenceSplit> findByOccurrenceGroupId(UUID groupId) {
        return jpaRepository.findByOccurrenceGroupId(groupId)
                .stream()
                .map(expenseMapper::toDomain)
                .toList();
    }

    @Override
    public void cancelSplitsForOccurrence(UUID occurrenceId) {
        jpaRepository.cancelSplitsByOccurrenceId(occurrenceId);
    }

    @Override
    public List<ExpenseOccurrenceSplit> findByOccurrenceIdIn(List<UUID> occurrenceIds) {
        if (occurrenceIds == null || occurrenceIds.isEmpty()) {
            return List.of();
        }

        // 1. Consulta la base de datos (SELECT ... WHERE occurrence_id IN (...))
        List<ExpenseOccurrenceSplitEntity> entities = jpaRepository.findByOccurrenceIdIn(occurrenceIds);

        // 2. Transforma las entidades JPA a modelos de dominio inmutables (records)
        return entities.stream()
                .map(expenseMapper::toDomain)
                .toList();
    }
}
