package com.example.SplitLoop.expense.exception;


import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class CannotCancelPaidOccurrenceException extends BusinessException {

    public CannotCancelPaidOccurrenceException() {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.CANNOT_CANCEL_PAID_OCCURRENCE,
                "No se puede cancelar una ocurrencia de gasto que ya ha sido pagada "
        );
    }

}
