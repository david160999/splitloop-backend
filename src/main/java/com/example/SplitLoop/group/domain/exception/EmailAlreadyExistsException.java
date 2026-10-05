package com.example.SplitLoop.group.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
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
