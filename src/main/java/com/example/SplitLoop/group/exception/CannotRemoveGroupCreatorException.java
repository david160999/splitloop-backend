package com.example.SplitLoop.group.exception;


import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class CannotRemoveGroupCreatorException extends BusinessException {

    public CannotRemoveGroupCreatorException() {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.CANNOT_REMOVE_GROUP_CREATOR,
                "No se puede eliminar al creador del grupo"
        );
    }

}