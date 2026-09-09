package com.example.SplitLoop.user.exception;

import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
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
