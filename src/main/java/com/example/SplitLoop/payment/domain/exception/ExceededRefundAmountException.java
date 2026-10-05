package com.example.SplitLoop.payment.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

public class ExceededRefundAmountException extends BusinessException {

    public ExceededRefundAmountException() {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.EXCEEDED_REFUND_AMOUNT,
                "El importe del reembolso no puede superar el monto de la transacción original"
        );
    }
}
