package com.example.SplitLoop.payment.domain.validator;

import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.payment.domain.entity.Payment;

import java.math.BigDecimal;

public interface PaymentValidator {

    void validatePayment(Payment payment);

    void validateCanUpdate(Payment payment, BigDecimal newAmount);

    void validateCanRefund(
            Payment payment,
            BigDecimal refundAmount);

    void validateCanRegister(Payment payment, GroupMember member);
}
