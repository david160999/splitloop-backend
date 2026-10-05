package com.example.SplitLoop.group.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class LastAdminCannotLeaveGroupException extends BusinessException {

    public LastAdminCannotLeaveGroupException() {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.LAST_ADMIN_CANNOT_LEAVE_GROUP,
                "El último administrador no puede abandonar el grupo. Debe promover a otro miembro o eliminar el grupo."
        );
    }

    public LastAdminCannotLeaveGroupException(String message) {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.LAST_ADMIN_CANNOT_LEAVE_GROUP,
                message
        );
    }
}
