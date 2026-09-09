package com.example.SplitLoop.user.exception;

import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends BusinessException {

    public InvalidPasswordException() {
        super(
                HttpStatus.UNAUTHORIZED,
                ErrorCode.INVALID_PASSWORD,
                "La contraseña es incorrecta"
        );
    }

}