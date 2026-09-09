package com.example.SplitLoop.user.exception;

import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class UsernameRequiredException extends BusinessException {

    public UsernameRequiredException() {
        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.USERNAME_REQUIRED,
                "El nombre de usuario es obligatorio"
        );
    }

}
