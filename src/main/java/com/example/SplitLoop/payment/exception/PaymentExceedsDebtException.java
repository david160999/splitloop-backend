package com.example.SplitLoop.payment.exception;

import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
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
