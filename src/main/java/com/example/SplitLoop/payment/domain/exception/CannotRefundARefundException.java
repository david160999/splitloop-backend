package com.example.SplitLoop.payment.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class CannotRefundARefundException extends BusinessException {

    public CannotRefundARefundException() {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.CANNOT_REFUND_A_REFUND,
                "No se puede realizar un reembolso sobre una transacción que ya es un reembolso"
        );
    }
}