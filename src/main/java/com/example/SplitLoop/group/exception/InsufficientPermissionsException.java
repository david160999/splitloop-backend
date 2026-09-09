package com.example.SplitLoop.group.exception;


import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class InsufficientPermissionsException extends BusinessException {

    public InsufficientPermissionsException() {
        super(
                HttpStatus.FORBIDDEN,
                ErrorCode.INSUFFICIENT_PERMISSIONS,
                "No tienes permisos para realizar esta acción"
        );
    }

}