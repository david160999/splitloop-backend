package com.example.SplitLoop.group.exception;


import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class LastAdminInGroupException extends BusinessException {

    public LastAdminInGroupException() {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.LAST_ADMIN_IN_GROUP,
                "No puedes realizar esta acción porque eres el último administrador del grupo"
        );
    }

}