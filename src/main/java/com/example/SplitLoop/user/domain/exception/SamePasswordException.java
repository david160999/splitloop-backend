package com.example.SplitLoop.user.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class SamePasswordException extends BusinessException {

    public SamePasswordException() {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.SAME_PASSWORD,
                "La nueva contraseña debe ser diferente de la actual"
        );
    }

}
