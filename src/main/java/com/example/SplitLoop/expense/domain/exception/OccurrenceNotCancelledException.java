package com.example.SplitLoop.expense.domain.exception;


import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class OccurrenceNotCancelledException extends BusinessException {

    public OccurrenceNotCancelledException(UUID occurrenceId) {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.OCCURRENCE_NOT_CANCELLED,
                String.format(
                        "La ocurrencia de gasto %s no está cancelada",
                        occurrenceId
                )
        );
    }

}
