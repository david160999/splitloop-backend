package com.example.SplitLoop.payment.infrastructure.presentation.rest.specification;

import com.example.SplitLoop.payment.application.dto.query.GetPaymentsQuery;
import com.example.SplitLoop.payment.infrastructure.persistence.entity.PaymentEntity;
import com.example.SplitLoop.payment.domain.model.PaymentType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public class PaymentSpecification {

    public static Specification<PaymentEntity> withFilters(GetPaymentsQuery query) {

        return Specification
                .where(hasGroup(query.groupId()))
                .and(hasUser(query.userId()))
                .and(hasOccurrence(query.occurrenceId()))
                .and(hasType(query.type()))
                .and(paidAfter(query.from()))
                .and(paidBefore(query.to()));
    }

    public static Specification<PaymentEntity> hasGroup(UUID groupId) {

        return (root, q, cb) ->

                groupId == null
                        ? null
                        : cb.equal(
                        root.get("occurrence")
                                .get("group")
                                .get("id"),
                        groupId);
    }

    public static Specification<PaymentEntity> hasUser(UUID userId) {

        return (root, q, cb) -> {

            if (userId == null) {
                return null;
            }

            return cb.or(
                    cb.equal(root.get("fromUser").get("id"), userId),
                    cb.equal(root.get("toUser").get("id"), userId)
            );
        };
    }

    public static Specification<PaymentEntity> hasOccurrence(UUID occurrenceId) {

        return (root, q, cb) ->

                occurrenceId == null
                        ? null
                        : cb.equal(
                        root.get("occurrence").get("id"),
                        occurrenceId);
    }

    public static Specification<PaymentEntity> hasType(PaymentType type) {

        return (root, q, cb) ->

                type == null
                        ? null
                        : cb.equal(root.get("type"), type);
    }

    public static Specification<PaymentEntity> paidAfter(LocalDate from) {

        return (root, q, cb) ->

                from == null
                        ? null
                        : cb.greaterThanOrEqualTo(
                        root.get("paidAt"),
                        from.atStartOfDay());
    }

    public static Specification<PaymentEntity> paidBefore(LocalDate to) {

        return (root, q, cb) ->

                to == null
                        ? null
                        : cb.lessThan(
                        root.get("paidAt"),
                        to.plusDays(1).atStartOfDay());
    }

    private PaymentSpecification() {
    }
}
