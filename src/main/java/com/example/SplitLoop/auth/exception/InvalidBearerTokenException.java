package com.example.SplitLoop.auth.exception;

import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidBearerTokenException extends BusinessException {

    public InvalidBearerTokenException() {
        super(
                HttpStatus.UNAUTHORIZED,
                ErrorCode.INVALID_BEARER_TOKEN,
                "El token de acceso es inválido"
        );
    }

}
