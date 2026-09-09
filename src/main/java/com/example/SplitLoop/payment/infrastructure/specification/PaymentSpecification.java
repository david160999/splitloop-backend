package com.example.SplitLoop.payment.infrastructure.specification;

import com.example.SplitLoop.payment.controller.query.GetPaymentsQuery;
import com.example.SplitLoop.payment.domain.entity.Payment;
import com.example.SplitLoop.payment.domain.entity.PaymentType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public class PaymentSpecification {

    public static Specification<Payment> withFilters(GetPaymentsQuery query) {

        return Specification
                .where(hasGroup(query.getGroupId()))
                .and(hasUser(query.getUserId()))
                .and(hasOccurrence(query.getOccurrenceId()))
                .and(hasType(query.getType()))
                .and(paidAfter(query.getFrom()))
                .and(paidBefore(query.getTo()));
    }

    public static Specification<Payment> hasGroup(UUID groupId) {

        return (root, q, cb) ->

                groupId == null
                        ? null
                        : cb.equal(
                        root.get("occurrence")
                                .get("group")
                                .get("id"),
                        groupId);
    }

    public static Specification<Payment> hasUser(UUID userId) {

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

    public static Specification<Payment> hasOccurrence(UUID occurrenceId) {

        return (root, q, cb) ->

                occurrenceId == null
                        ? null
                        : cb.equal(
                        root.get("occurrence").get("id"),
                        occurrenceId);
    }

    public static Specification<Payment> hasType(PaymentType type) {

        return (root, q, cb) ->

                type == null
                        ? null
                        : cb.equal(root.get("type"), type);
    }

    public static Specification<Payment> paidAfter(LocalDate from) {

        return (root, q, cb) ->

                from == null
                        ? null
                        : cb.greaterThanOrEqualTo(
                        root.get("paidAt"),
                        from.atStartOfDay());
    }

    public static Specification<Payment> paidBefore(LocalDate to) {

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
