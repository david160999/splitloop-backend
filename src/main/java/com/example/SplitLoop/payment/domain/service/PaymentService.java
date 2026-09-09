package com.example.SplitLoop.payment.domain.service;

import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplit;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.payment.domain.entity.Payment;
import com.example.SplitLoop.user.domain.entity.User;

import java.math.BigDecimal;

public interface PaymentService {

    Payment registerPayment(Payment payment, GroupMember member);

    Payment refundPayment(
            Payment originalPayment,
            GroupMember member, User createdBy,
            BigDecimal amount);

    void updateSplitStatus(ExpenseOccurrenceSplit split);

    void updateOccurrenceStatus(ExpenseOccurrence occurrence);

    BigDecimal calculatePaidAmount(
            ExpenseOccurrenceSplit split);

    BigDecimal calculateRemainingDebt(ExpenseOccurrenceSplit split);
}
