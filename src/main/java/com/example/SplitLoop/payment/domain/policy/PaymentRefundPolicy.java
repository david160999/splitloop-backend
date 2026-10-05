package com.example.SplitLoop.payment.domain.policy;

import com.example.SplitLoop.payment.domain.model.Payment;

import java.math.BigDecimal;

public interface PaymentRefundPolicy {

    void validateCanRefund(Payment originalPayment, BigDecimal refundAmount);
}
