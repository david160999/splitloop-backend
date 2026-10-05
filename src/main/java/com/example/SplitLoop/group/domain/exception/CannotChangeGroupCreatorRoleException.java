package com.example.SplitLoop.group.domain.exception;


import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class CannotChangeGroupCreatorRoleException extends BusinessException {

    public CannotChangeGroupCreatorRoleException() {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.CANNOT_CHANGE_GROUP_CREATOR_ROLE,
                "No se puede cambiar el rol del creador del grupo"
        );
    }

}