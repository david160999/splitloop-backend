package com.example.SplitLoop.expense.infrastructure.persistence.adapter;

import com.example.SplitLoop.expense.domain.model.RecurringExpense;
import com.example.SplitLoop.expense.domain.model.RecurringExpenseStatus;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.RecurringExpenseEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.jpa.SpringDataRecurringExpenseRepository;
import com.example.SplitLoop.expense.infrastructure.persistence.mapper.ExpensePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RecurringExpenseRepositoryAdapter implements RecurringExpenseRepository {

    private final SpringDataRecurringExpenseRepository jpaRepository;
    private final ExpensePersistenceMapper expenseMapper;

    @Override
    public RecurringExpense save(RecurringExpense recurringExpense) {
        RecurringExpenseEntity entity = expenseMapper.toEntity(recurringExpense);
        RecurringExpenseEntity savedEntity = jpaRepository.save(entity);
        return expenseMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<RecurringExpense> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(expenseMapper::toDomain);
    }

    @Override
    public List<RecurringExpense> findByGroupId(UUID groupId) {
        return jpaRepository.findByGroupId(groupId)
                .stream()
                .map(expenseMapper::toDomain)
                .toList();
    }

    @Override
    public List<RecurringExpense> findByGroupIdAndStatus(UUID groupId, RecurringExpenseStatus status) {
        return jpaRepository.findByGroupIdAndStatus(groupId, status)
                .stream()
                .map(expenseMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByGroupIdAndStatus(UUID groupId, RecurringExpenseStatus status) {
        return jpaRepository.existsByGroupIdAndStatus(groupId, status);
    }

    @Override
    public int countByGroupIdAndStatus(UUID groupId, RecurringExpenseStatus status) {
        return jpaRepository.countByGroupIdAndStatus(groupId, status);
    }

    @Override
    public List<RecurringExpense> findByStatus(RecurringExpenseStatus status) {
        return jpaRepository.findByStatus(status)
                .stream()
                .map(expenseMapper::toDomain)
                .toList();
    }

    @Override
    public List<RecurringExpense> findByStatusAndStartDateLessThanEqual(RecurringExpenseStatus status, LocalDate date) {
        return jpaRepository.findByStatusAndStartDateLessThanEqual(status, date)
                .stream()
                .map(expenseMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
