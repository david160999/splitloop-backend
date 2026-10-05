package com.example.SplitLoop.user.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class PasswordRequiredException extends BusinessException {

    public PasswordRequiredException() {
        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.PASSWORD_REQUIRED,
                "La contraseña es obligatoria"
        );
    }

}
