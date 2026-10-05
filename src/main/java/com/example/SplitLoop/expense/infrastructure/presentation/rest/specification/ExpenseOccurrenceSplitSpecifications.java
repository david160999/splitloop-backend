package com.example.SplitLoop.expense.infrastructure.presentation.rest.specification;

import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceSplitEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public class ExpenseOccurrenceSplitSpecifications {

    public static Specification<ExpenseOccurrenceSplitEntity> hasGroup(UUID groupId) {
        return (root, query, cb) ->
                cb.equal(
                        root.get("occurrence")
                                .get("group")
                                .get("id"),
                        groupId);
    }

    public static Specification<ExpenseOccurrenceSplitEntity> dueDateAfter(LocalDate from) {

        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(
                        root.get("occurrence").get("dueDate"),
                        from);
    }

    public static Specification<ExpenseOccurrenceSplitEntity> dueDateBefore(LocalDate to) {

        return (root, query, cb) ->
                cb.lessThanOrEqualTo(
                        root.get("occurrence").get("dueDate"),
                        to);
    }
}
