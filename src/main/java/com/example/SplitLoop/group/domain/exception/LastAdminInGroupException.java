package com.example.SplitLoop.group.domain.exception;


import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
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