package com.example.SplitLoop.auth.exception;

import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class RefreshTokenRequiredException extends BusinessException {

    public RefreshTokenRequiredException(String s) {
        super(
                HttpStatus.UNAUTHORIZED,
                ErrorCode.REFRESH_TOKEN_REQUIRED,
                "El refresh token es requerido"
        );
    }

}
