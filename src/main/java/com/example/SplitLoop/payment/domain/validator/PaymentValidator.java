package com.example.SplitLoop.payment.domain.validator;

import com.example.SplitLoop.group.infrastructure.persistence.entity.GroupMemberEntity;
import com.example.SplitLoop.payment.infrastructure.persistence.entity.PaymentEntity;

import java.math.BigDecimal;

public interface PaymentValidator {

    void validatePayment(PaymentEntity paymentEntity);

    void validateCanUpdate(PaymentEntity paymentEntity, BigDecimal newAmount);

    void validateCanRefund(
            PaymentEntity paymentEntity,
            BigDecimal refundAmount);

    void validateCanRegister(PaymentEntity paymentEntity, GroupMemberEntity member);
}
