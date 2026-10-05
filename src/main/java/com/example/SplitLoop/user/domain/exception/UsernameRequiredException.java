package com.example.SplitLoop.user.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
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
