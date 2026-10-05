package com.example.SplitLoop.user.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class PasswordsDoNotMatchException extends BusinessException {

    public PasswordsDoNotMatchException() {
        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.PASSWORDS_DO_NOT_MATCH,
                "Las contraseñas no coinciden"
        );
    }

}
