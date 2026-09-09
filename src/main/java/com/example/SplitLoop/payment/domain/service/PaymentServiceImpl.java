package com.example.SplitLoop.payment.domain.service;

import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplitStatus;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceStatus;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceSplitRepository;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.payment.domain.entity.Payment;
import com.example.SplitLoop.payment.domain.entity.PaymentType;
import com.example.SplitLoop.payment.domain.repository.PaymentRepository;
import com.example.SplitLoop.payment.domain.validator.PaymentValidator;
import com.example.SplitLoop.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ExpenseOccurrenceSplitRepository splitRepository;
    private final ExpenseOccurrenceRepository occurrenceRepository;

    private final PaymentValidator validator;

    @Override
    public Payment registerPayment(Payment payment, GroupMember member) {

        validator.validateCanRegister(payment, member);

        Payment saved = paymentRepository.save(payment);

        updateSplitStatus(saved.getSplit());

        updateOccurrenceStatus(saved.getOccurrence());

        return saved;
    }

    @Override
    public Payment refundPayment(Payment originalPayment, GroupMember member, User createdBy, BigDecimal amount) {

        validator.validateCanRefund(originalPayment, amount);

        Payment refund = Payment.builder()
                .occurrence(originalPayment.getOccurrence())
                .split(originalPayment.getSplit())
                .fromUser(originalPayment.getFromUser())
                .toUser(originalPayment.getToUser())
                .createdBy(createdBy)
                .amount(amount.negate())
                .type(PaymentType.REFUND)
                .note(originalPayment.getNote())
                .build();

        Payment saved = paymentRepository.save(refund);

        updateSplitStatus(saved.getSplit());

        updateOccurrenceStatus(saved.getOccurrence());

        return saved;
    }

    @Override
    public void updateSplitStatus(ExpenseOccurrenceSplit split) {

        BigDecimal paid = calculatePaidAmount(split);

        split.setAmountPaid(paid);

        if (paid.compareTo(BigDecimal.ZERO) == 0) {

            split.setStatus(ExpenseOccurrenceSplitStatus.PENDING);

        } else if (paid.compareTo(split.getAmountOwed()) >= 0) {

            split.setStatus(ExpenseOccurrenceSplitStatus.PAID);

        } else {

            split.setStatus(ExpenseOccurrenceSplitStatus.PARTIALLY_PAID);
        }

        splitRepository.save(split);
    }

    @Override
    public void updateOccurrenceStatus(ExpenseOccurrence occurrence) {

        List<ExpenseOccurrenceSplit> splits =
                splitRepository.findByOccurrence(occurrence);

        boolean allPaid = splits.stream()
                .allMatch(s -> s.getStatus() == ExpenseOccurrenceSplitStatus.PAID);

        boolean anyPaid = splits.stream()
                .anyMatch(s ->
                        s.getStatus() != ExpenseOccurrenceSplitStatus.PENDING);

        if (allPaid) {

            occurrence.setStatus(ExpenseOccurrenceStatus.PAID);

        } else if (anyPaid) {

            occurrence.setStatus(ExpenseOccurrenceStatus.PARTIALLY_PAID);

        } else {

            occurrence.setStatus(ExpenseOccurrenceStatus.PENDING);
        }

        occurrenceRepository.save(occurrence);
    }

    @Override
    public BigDecimal calculatePaidAmount(
            ExpenseOccurrenceSplit split) {

        return paymentRepository.sumAmountBySplit(split);

    }

    @Override
    public BigDecimal calculateRemainingDebt(
            ExpenseOccurrenceSplit split) {

        return split.getAmountOwed()
                .subtract(calculatePaidAmount(split));
    }
}