package com.example.SplitLoop.expense.infrastructure.persistence.mapper;

import com.example.SplitLoop.expense.domain.model.*;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceSplitEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.RecurringExpenseEntity;
import com.example.SplitLoop.expense.infrastructure.persistence.entity.RecurringExpenseParticipantEntity;
import com.example.SplitLoop.group.infrastructure.persistence.mapper.GroupPersistenceMapper;
import com.example.SplitLoop.user.infrastructure.persistence.mapper.UserPersistenceMapper;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {
                UserPersistenceMapper.class,
                GroupPersistenceMapper.class
        }
)
public interface ExpensePersistenceMapper {

    // -------------------------------------------------------------------
    // RecurringExpense
    // -------------------------------------------------------------------
    RecurringExpenseEntity toEntity(RecurringExpense domain);
    RecurringExpense toDomain(RecurringExpenseEntity entity);

    /**
     * Se ejecuta automáticamente DESPUÉS de mapear RecurringExpense -> RecurringExpenseEntity.
     * Asigna la relación bidireccional del padre a cada participante.
     */
    @AfterMapping
    default void linkParticipants(@MappingTarget RecurringExpenseEntity entity) {
        if (entity.getParticipants() != null) {
            entity.getParticipants().forEach(participant -> participant.setRecurringExpense(entity));
        }
    }

    // -------------------------------------------------------------------
    // RecurringExpenseParticipant
    // -------------------------------------------------------------------
    RecurringExpenseParticipantEntity toEntity(RecurringExpenseParticipant domain);
    RecurringExpenseParticipant toDomain(RecurringExpenseParticipantEntity entity);

    // -------------------------------------------------------------------
    // ExpenseOccurrence
    // -------------------------------------------------------------------
    ExpenseOccurrenceEntity toEntity(ExpenseOccurrence domain);
    ExpenseOccurrence toDomain(ExpenseOccurrenceEntity entity);

    // -------------------------------------------------------------------
    // ExpenseOccurrenceSplit
    // -------------------------------------------------------------------
    ExpenseOccurrenceSplitEntity toEntity(ExpenseOccurrenceSplit domain);
    ExpenseOccurrenceSplit toDomain(ExpenseOccurrenceSplitEntity entity);

    // 2. Métodos auxiliares para la conversión Money <-> BigDecimal
    default BigDecimal map(Money money) {
        return money != null ? money.amount() : null;
    }

    default Money map(BigDecimal amount) {
        return amount != null ? Money.of(amount) : null;
    }

    List<ExpenseOccurrenceEntity> toEntityList(List<ExpenseOccurrence> updatedOccurrences);
}

