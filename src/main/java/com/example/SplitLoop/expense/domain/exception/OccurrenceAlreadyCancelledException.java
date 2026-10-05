package com.example.SplitLoop.expense.domain.exception;


import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class OccurrenceAlreadyCancelledException extends BusinessException {

    public OccurrenceAlreadyCancelledException() {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.OCCURRENCE_ALREADY_CANCELLED,
                "La ocurrencia del gasto ya está cancelada"
        );
    }

}
