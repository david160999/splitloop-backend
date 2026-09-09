package com.example.SplitLoop.expense.infrastructure.specification;

import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public class ExpenseOccurrenceSpecifications {

    public static Specification<ExpenseOccurrence> hasRecurringExpense(
            UUID recurringExpenseId) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("recurringExpense").get("id"),
                        recurringExpenseId);
    }

    public static Specification<ExpenseOccurrence> hasStatus(
            ExpenseOccurrenceStatus status) {

        return (root, query, cb) ->
                cb.equal(root.get("status"), status);
    }

    public static Specification<ExpenseOccurrence> dueDateAfter(
            LocalDate from) {

        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(
                        root.get("dueDate"),
                        from);
    }

    public static Specification<ExpenseOccurrence> dueDateBefore(
            LocalDate to) {

        return (root, query, cb) ->
                cb.lessThanOrEqualTo(
                        root.get("dueDate"),
                        to);
    }


}