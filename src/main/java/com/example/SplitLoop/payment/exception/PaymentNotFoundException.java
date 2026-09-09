package com.example.SplitLoop.payment.exception;

import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class PaymentNotFoundException extends BusinessException {

    public PaymentNotFoundException(UUID paymentId) {
        super(
                HttpStatus.NOT_FOUND,
                ErrorCode.PAYMENT_NOT_FOUND,
                String.format(
                        "No existe ningún pago con id %s",
                        paymentId
                )
        );
    }

}