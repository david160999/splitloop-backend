package com.example.SplitLoop.payment.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidPaymentAmountException extends BusinessException {

    public InvalidPaymentAmountException() {
        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_PAYMENT_AMOUNT,
                "El importe del pago debe ser mayor a cero"
        );
    }

    public InvalidPaymentAmountException(String message) {
        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_PAYMENT_AMOUNT,
                message
        );
    }
}
