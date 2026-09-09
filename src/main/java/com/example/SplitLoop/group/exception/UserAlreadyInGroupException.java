package com.example.SplitLoop.group.exception;

import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UserAlreadyInGroupException extends BusinessException {

    public UserAlreadyInGroupException(UUID userId, UUID groupId) {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.USER_ALREADY_IN_GROUP,
                String.format(
                        "El usuario %s ya pertenece al grupo %s",
                        userId,
                        groupId
                )
        );
    }

}
