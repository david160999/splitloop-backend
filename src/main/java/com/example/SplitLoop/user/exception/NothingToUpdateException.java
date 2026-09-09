package com.example.SplitLoop.user.exception;

import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class NothingToUpdateException extends BusinessException {

    public NothingToUpdateException() {
        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.NOTHING_TO_UPDATE,
                "No hay cambios para actualizar"
        );
    }

}
