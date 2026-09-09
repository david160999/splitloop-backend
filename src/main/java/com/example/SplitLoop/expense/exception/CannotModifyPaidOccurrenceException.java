package com.example.SplitLoop.expense.exception;

import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class CannotModifyPaidOccurrenceException extends BusinessException {

    public CannotModifyPaidOccurrenceException(UUID occurrenceId) {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.CANNOT_MODIFY_PAID_OCCURRENCE,
                String.format(
                        "No se puede modificar la ocurrencia de gasto %s porque ya ha sido pagada",
                        occurrenceId
                )
        );
    }

}
