package com.example.SplitLoop.payment.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidRefundAmountException extends BusinessException {

    public InvalidRefundAmountException() {
        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_REFUND_AMOUNT,
                "El importe del reembolso debe ser mayor a cero"
        );
    }

    public InvalidRefundAmountException(String customMessage) {
        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_REFUND_AMOUNT,
                customMessage
        );
    }
}
