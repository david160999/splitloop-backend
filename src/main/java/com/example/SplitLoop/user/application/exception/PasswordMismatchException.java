package com.example.SplitLoop.user.application.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class PasswordMismatchException extends BusinessException {

    public PasswordMismatchException() {
        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.PASSWORD_MISMATCH,
                "La nueva contraseña y la confirmación no coinciden"
        );
    }

    public PasswordMismatchException(String message) {
        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.PASSWORD_MISMATCH,
                message
        );
    }
}