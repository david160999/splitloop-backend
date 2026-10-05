package com.example.SplitLoop.payment.infrastructure.persistence.policy;

import com.example.SplitLoop.payment.domain.exception.CannotRefundARefundException;
import com.example.SplitLoop.payment.domain.exception.ExceededRefundAmountException;
import com.example.SplitLoop.payment.domain.exception.InvalidRefundAmountException;
import com.example.SplitLoop.payment.domain.model.Payment;
import com.example.SplitLoop.payment.domain.model.PaymentType;
import com.example.SplitLoop.payment.domain.policy.PaymentRefundPolicy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MaxAmountRefundPolicy implements PaymentRefundPolicy {

    @Override
    public void validateCanRefund(Payment originalPayment, BigDecimal refundAmount) {
        if (refundAmount == null || refundAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRefundAmountException("El monto a reembolsar debe ser mayor a cero.");
        }

        if (originalPayment.type() == PaymentType.REFUND) {
            throw new CannotRefundARefundException();
        }

        if (originalPayment.amount().abs().compareTo(refundAmount) < 0) {
            throw new ExceededRefundAmountException();
        }
    }
}
