package com.example.SplitLoop.payment.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class PaymentExceedsDebtException extends BusinessException {

    public PaymentExceedsDebtException() {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.PAYMENT_EXCEEDS_DEBT,
                "El importe del pago no puede superar la deuda pendiente"
        );
    }

}
