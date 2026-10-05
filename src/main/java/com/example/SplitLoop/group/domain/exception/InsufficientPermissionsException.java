package com.example.SplitLoop.group.domain.exception;


import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
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