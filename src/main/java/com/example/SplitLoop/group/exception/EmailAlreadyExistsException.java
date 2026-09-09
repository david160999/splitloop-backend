package com.example.SplitLoop.group.exception;

import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends BusinessException {

    public EmailAlreadyExistsException(String email) {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.EMAIL_ALREADY_EXISTS,
                String.format(
                        "El email '%s' ya está registrado",
                        email
                )
        );
    }

}
