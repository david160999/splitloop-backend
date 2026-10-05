package com.example.SplitLoop.payment.domain.validator;

import com.example.SplitLoop.expense.infrastructure.persistence.entity.ExpenseOccurrenceSplitEntity;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceStatus;
import com.example.SplitLoop.expense.domain.exception.OccurrenceAlreadyCancelledException;
import com.example.SplitLoop.expense.domain.exception.SamePaidByException;
import com.example.SplitLoop.group.infrastructure.persistence.entity.GroupMemberEntity;
import com.example.SplitLoop.group.domain.exception.InsufficientPermissionsException;
import com.example.SplitLoop.payment.infrastructure.persistence.entity.PaymentEntity;
import com.example.SplitLoop.payment.domain.exception.PaymentExceedsDebtException;
import com.example.SplitLoop.payment.domain.exception.RefundExceedsPaymentException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentValidatorImpl implements PaymentValidator {

    @Override
    public void validatePayment(PaymentEntity paymentEntity) {

        validateAmount(paymentEntity.getAmount());

        if (paymentEntity.getOccurrence() == null) {
            throw new IllegalArgumentException("Occurrence is required.");
        }

        if (paymentEntity.getSplit() == null) {
            throw new IllegalArgumentException("Split is required.");
        }

        if (paymentEntity.getFromUser() == null) {
            throw new IllegalArgumentException("From user is required.");
        }

        if (paymentEntity.getToUser() == null) {
            throw new IllegalArgumentException("To user is required.");
        }

        if (paymentEntity.getCreatedBy() == null) {
            throw new IllegalArgumentException("Created by is required.");
        }

        if (paymentEntity.getFromUser().equals(paymentEntity.getToUser())) {
            throw new SamePaidByException();
        }
    }


    @Override
    public void validateCanUpdate(PaymentEntity paymentEntity, BigDecimal newAmount) {

        validateAmount(newAmount);

        validateOccurrenceOpen(paymentEntity);

        BigDecimal remaining =
                paymentEntity.getSplit()
                        .getAmountOwed()
                        .subtract(paymentEntity.getSplit().getAmountPaid())
                        .add(paymentEntity.getAmount());

        if (newAmount.compareTo(remaining) > 0) {
            throw new PaymentExceedsDebtException();
        }
    }

    @Override
    public void validateCanRefund(
            PaymentEntity paymentEntity,
            BigDecimal refundAmount) {

        validateAmount(refundAmount);

        if (refundAmount.compareTo(paymentEntity.getAmount()) > 0) {
            throw new RefundExceedsPaymentException();
        }
    }

    @Override
    public void validateCanRegister(PaymentEntity paymentEntity, GroupMemberEntity member) {

        validateGroupMember(member);

        validatePayment(paymentEntity);

        validateOccurrenceOpen(paymentEntity);

        validateSplitMatches(paymentEntity);

        validateRemainingDebt(
                paymentEntity.getSplit(),
                paymentEntity.getAmount());
    }

    private void validateGroupMember(GroupMemberEntity member) {

        if (member == null) {
            throw new InsufficientPermissionsException();
        }
    }

    private void validateOccurrenceOpen(PaymentEntity paymentEntity) {

        if (paymentEntity.getOccurrence().getStatus()
                == ExpenseOccurrenceStatus.CANCELLED) {

            throw new OccurrenceAlreadyCancelledException();
        }
    }

    private void validateSplitMatches(PaymentEntity paymentEntity) {

        if (!paymentEntity.getSplit().getOccurrence()
                .equals(paymentEntity.getOccurrence())) {

            throw new IllegalArgumentException(
                    "Split does not belong to occurrence.");
        }

        if (!paymentEntity.getSplit().getUser()
                .equals(paymentEntity.getFromUser())) {

            throw new IllegalArgumentException(
                    "Invalid payer.");
        }

        if (!paymentEntity.getOccurrence().getPaidBy()
                .equals(paymentEntity.getToUser())) {

            throw new SamePaidByException();
        }
    }

    private void validateRemainingDebt(
            ExpenseOccurrenceSplitEntity split,
            BigDecimal amount) {

        BigDecimal remaining = split.getAmountOwed()
                .subtract(split.getAmountPaid());

        if (amount.compareTo(remaining) > 0) {
            throw new PaymentExceedsDebtException();
        }
    }

    private void validateAmount(BigDecimal amount) {

        if (amount == null
                || amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Invalid amount.");
        }
    }
}

