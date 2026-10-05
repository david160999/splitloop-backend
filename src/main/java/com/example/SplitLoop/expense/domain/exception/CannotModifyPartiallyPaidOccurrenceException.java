package com.example.SplitLoop.expense.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class CannotModifyPartiallyPaidOccurrenceException extends BusinessException {

    public CannotModifyPartiallyPaidOccurrenceException() {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.CANNOT_MODIFY_PARTIALLY_PAID_OCCURRENCE,
                "No se puede modificar una ocurrencia de gasto que ha sido pagada parcialmente"
        );
    }

}