package com.example.SplitLoop.user.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class WeakPasswordException extends BusinessException {

    public WeakPasswordException() {
        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.WEAK_PASSWORD,
                "La contraseña no cumple los requisitos de seguridad"
        );
    }

}
