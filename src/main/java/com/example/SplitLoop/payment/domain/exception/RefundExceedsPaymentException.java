package com.example.SplitLoop.payment.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class RefundExceedsPaymentException extends BusinessException {

    public RefundExceedsPaymentException() {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.REFUND_EXCEEDS_PAYMENT,
                "El importe del reembolso no puede superar el importe pagado"
        );
    }

}