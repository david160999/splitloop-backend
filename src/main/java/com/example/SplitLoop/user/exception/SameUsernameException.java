package com.example.SplitLoop.user.exception;

import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class SameUsernameException extends BusinessException {

    public SameUsernameException() {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.SAME_USERNAME,
                "El nuevo nombre de usuario debe ser diferente del actual"
        );
    }

}