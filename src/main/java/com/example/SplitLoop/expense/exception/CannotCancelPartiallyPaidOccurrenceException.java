package com.example.SplitLoop.expense.exception;


import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class CannotCancelPartiallyPaidOccurrenceException extends BusinessException {

    public CannotCancelPartiallyPaidOccurrenceException() {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.CANNOT_CANCEL_PARTIALLY_PAID_OCCURRENCE,
                "No se puede cancelar una ocurrencia de gasto que ha sido pagada parcialmente"
        );
    }

}
