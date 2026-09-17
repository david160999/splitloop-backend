package com.example.SplitLoop.auth.exception;

import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class TokenExpiredException extends BusinessException {

    public TokenExpiredException(String s) {
        super(
                HttpStatus.UNAUTHORIZED,
                ErrorCode.TOKEN_EXPIRED,
                "El Refresh Token ha expirado"
        );
    }

}