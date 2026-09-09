package com.example.SplitLoop.payment.domain.validator;

import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceStatus;
import com.example.SplitLoop.expense.exception.OccurrenceAlreadyCancelledException;
import com.example.SplitLoop.expense.exception.SamePaidByException;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.group.exception.InsufficientPermissionsException;
import com.example.SplitLoop.payment.domain.entity.Payment;
import com.example.SplitLoop.payment.exception.PaymentExceedsDebtException;
import com.example.SplitLoop.payment.exception.RefundExceedsPaymentException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentValidatorImpl implements PaymentValidator {

    @Override
    public void validatePayment(Payment payment) {

        validateAmount(payment.getAmount());

        if (payment.getOccurrence() == null) {
            throw new IllegalArgumentException("Occurrence is required.");
        }

        if (payment.getSplit() == null) {
            throw new IllegalArgumentException("Split is required.");
        }

        if (payment.getFromUser() == null) {
            throw new IllegalArgumentException("From user is required.");
        }

        if (payment.getToUser() == null) {
            throw new IllegalArgumentException("To user is required.");
        }

        if (payment.getCreatedBy() == null) {
            throw new IllegalArgumentException("Created by is required.");
        }

        if (payment.getFromUser().equals(payment.getToUser())) {
            throw new SamePaidByException();
        }
    }


    @Override
    public void validateCanUpdate(Payment payment, BigDecimal newAmount) {

        validateAmount(newAmount);

        validateOccurrenceOpen(payment);

        BigDecimal remaining =
                payment.getSplit()
                        .getAmountOwed()
                        .subtract(payment.getSplit().getAmountPaid())
                        .add(payment.getAmount());

        if (newAmount.compareTo(remaining) > 0) {
            throw new PaymentExceedsDebtException();
        }
    }

    @Override
    public void validateCanRefund(
            Payment payment,
            BigDecimal refundAmount) {

        validateAmount(refundAmount);

        if (refundAmount.compareTo(payment.getAmount()) > 0) {
            throw new RefundExceedsPaymentException();
        }
    }

    @Override
    public void validateCanRegister(Payment payment, GroupMember member) {

        validateGroupMember(member);

        validatePayment(payment);

        validateOccurrenceOpen(payment);

        validateSplitMatches(payment);

        validateRemainingDebt(
                payment.getSplit(),
                payment.getAmount());
    }

    private void validateGroupMember(GroupMember member) {

        if (member == null) {
            throw new InsufficientPermissionsException();
        }
    }

    private void validateOccurrenceOpen(Payment payment) {

        if (payment.getOccurrence().getStatus()
                == ExpenseOccurrenceStatus.CANCELLED) {

            throw new OccurrenceAlreadyCancelledException();
        }
    }

    private void validateSplitMatches(Payment payment) {

        if (!payment.getSplit().getOccurrence()
                .equals(payment.getOccurrence())) {

            throw new IllegalArgumentException(
                    "Split does not belong to occurrence.");
        }

        if (!payment.getSplit().getUser()
                .equals(payment.getFromUser())) {

            throw new IllegalArgumentException(
                    "Invalid payer.");
        }

        if (!payment.getOccurrence().getPaidBy()
                .equals(payment.getToUser())) {

            throw new SamePaidByException();
        }
    }

    private void validateRemainingDebt(
            ExpenseOccurrenceSplit split,
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

