package com.example.SplitLoop.payment.infrastructure.persistence.policy;

import com.example.SplitLoop.group.domain.model.GroupMember;
import com.example.SplitLoop.payment.domain.exception.InvalidPaymentAmountException;
import com.example.SplitLoop.payment.domain.exception.PaymentExceedsDebtException;
import com.example.SplitLoop.payment.domain.model.Payment;
import com.example.SplitLoop.payment.domain.policy.PaymentRegistrationPolicy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PaymentAmountPolicy implements PaymentRegistrationPolicy {

    @Override
    public void validateCanRegister(Payment payment, GroupMember member) {
        if (payment.amount() == null || payment.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPaymentAmountException();
        }

        // Si el monto del pago supera el importe pendiente del split
        if (payment.amount().compareTo(payment.split().getPendingAmount()) > 0) {
            throw new PaymentExceedsDebtException();
        }
    }
}
