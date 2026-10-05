package com.example.SplitLoop.expense.infrastructure.presentation.rest.specification;

import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceEntity;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public class ExpenseOccurrenceSpecifications {

    public static Specification<ExpenseOccurrenceEntity> hasRecurringExpense(
            UUID recurringExpenseId) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("recurringExpense").get("id"),
                        recurringExpenseId);
    }

    public static Specification<ExpenseOccurrenceEntity> hasStatus(
            ExpenseOccurrenceStatus status) {

        return (root, query, cb) ->
                cb.equal(root.get("status"), status);
    }

    public static Specification<ExpenseOccurrenceEntity> dueDateAfter(
            LocalDate from) {

        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(
                        root.get("dueDate"),
                        from);
    }

    public static Specification<ExpenseOccurrenceEntity> dueDateBefore(
            LocalDate to) {

        return (root, query, cb) ->
                cb.lessThanOrEqualTo(
                        root.get("dueDate"),
                        to);
    }


}