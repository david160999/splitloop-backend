package com.example.SplitLoop.auth.exception;

import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidRefreshTokenException extends BusinessException {

    public InvalidRefreshTokenException(String refreshTokenNoEncontrado) {
        super(
                HttpStatus.UNAUTHORIZED,
                ErrorCode.INVALID_REFRESH_TOKEN,
                "El token de actualización es inválido"
        );
    }

}