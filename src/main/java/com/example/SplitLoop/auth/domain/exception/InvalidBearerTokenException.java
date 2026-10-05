package com.example.SplitLoop.auth.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidBearerTokenException extends BusinessException {

    public InvalidBearerTokenException(String s) {
        super(
                HttpStatus.UNAUTHORIZED,
                ErrorCode.INVALID_BEARER_TOKEN,
                "El token de acceso es inválido"
        );
    }

}
