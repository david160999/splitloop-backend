package com.example.SplitLoop.expense.infrastructure.persistence.adapter;

import com.example.SplitLoop.expense.domain.model.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceCriteria;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceStatus;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.jpa.SpringDataExpenseOccurrenceRepository;
import com.example.SplitLoop.expense.infrastructure.persistence.mapper.ExpensePersistenceMapper;
import com.example.SplitLoop.expense.infrastructure.presentation.rest.specification.ExpenseOccurrenceSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ExpenseOccurrenceRepositoryAdapter implements ExpenseOccurrenceRepository {

    private final SpringDataExpenseOccurrenceRepository jpaRepository;
    private final ExpensePersistenceMapper expenseMapper;

    @Override
    public ExpenseOccurrence save(ExpenseOccurrence occurrence) {
        ExpenseOccurrenceEntity entity = expenseMapper.toEntity(occurrence);
        ExpenseOccurrenceEntity savedEntity = jpaRepository.save(entity);
        return expenseMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<ExpenseOccurrence> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(expenseMapper::toDomain);
    }

    @Override
    public List<ExpenseOccurrence> findByGroupId(UUID groupId) {
        return jpaRepository.findByGroupId(groupId)
                .stream()
                .map(expenseMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByGroupIdAndStatusNot(UUID groupId, ExpenseOccurrenceStatus status) {
        return jpaRepository.existsByGroupIdAndStatusNot(groupId, status);
    }

    @Override
    public List<ExpenseOccurrence> findByRecurringExpenseId(UUID recurringExpenseId) {
        return jpaRepository.findByRecurringExpenseId(recurringExpenseId)
                .stream()
                .map(expenseMapper::toDomain)
                .toList();
    }

    @Override
    public List<ExpenseOccurrence> findFutureOccurrences(UUID recurringExpenseId, LocalDate today) {
        return jpaRepository.findFutureOccurrences(recurringExpenseId, today, ExpenseOccurrenceStatus.CANCELLED)
                .stream()
                .map(expenseMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<ExpenseOccurrence> findTopByRecurringExpenseIdOrderByDueDateDesc(UUID recurringExpenseId) {
        return jpaRepository.findTopByRecurringExpenseIdOrderByDueDateDesc(recurringExpenseId)
                .map(expenseMapper::toDomain);
    }

    @Override
    public int countByGroupIdAndStatus(UUID groupId, ExpenseOccurrenceStatus status) {
        return jpaRepository.countByGroupIdAndStatus(groupId, status);
    }

    @Override
    public BigDecimal sumAmountByGroupId(UUID groupId) {
        return jpaRepository.sumAmountByGroupId(groupId);
    }

    @Override
    public Optional<LocalDate> findNextDueDateByGroupIdAndStatus(UUID groupId, ExpenseOccurrenceStatus status) {
        return jpaRepository.findNextDueDate(groupId, status);
    }

    @Override
    public List<ExpenseOccurrence> findAll(ExpenseOccurrenceCriteria criteria) {
        Specification<ExpenseOccurrenceEntity> spec = Specification.where(
                ExpenseOccurrenceSpecifications.hasRecurringExpense(criteria.recurringExpenseId()));

        if (criteria.status() != null) {
            spec = spec.and(ExpenseOccurrenceSpecifications.hasStatus(criteria.status()));
        }
        if (criteria.from() != null) {
            spec = spec.and(ExpenseOccurrenceSpecifications.dueDateAfter(criteria.from()));
        }
        if (criteria.to() != null) {
            spec = spec.and(ExpenseOccurrenceSpecifications.dueDateBefore(criteria.to()));
        }

        return jpaRepository.findAll(spec).stream()
                .map(expenseMapper::toDomain)
                .toList();
    }

    @Override
    public void saveAll(List<ExpenseOccurrence> updatedOccurrences) {
        if (updatedOccurrences == null || updatedOccurrences.isEmpty()) {
            return;
        }

        // 1. Mapear de Dominio a Entidades JPA
        List<ExpenseOccurrenceEntity> entities = expenseMapper.toEntityList(updatedOccurrences);

        // 2. Persistir todas las entidades en la BD
        jpaRepository.saveAll(entities);
    }
}

