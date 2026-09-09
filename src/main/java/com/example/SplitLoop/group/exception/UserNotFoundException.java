package com.example.SplitLoop.group.exception;

import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(UUID id) {
        super(
                HttpStatus.NOT_FOUND,
                ErrorCode.USER_NOT_FOUND,
                "No existe el usuario con id " + id
        );
    }

    public UserNotFoundException(String email) {
        super(
                HttpStatus.NOT_FOUND,
                ErrorCode.USER_NOT_FOUND,
                String.format("No existe ningún usuario con el email '%s'", email)
        );
    }

}