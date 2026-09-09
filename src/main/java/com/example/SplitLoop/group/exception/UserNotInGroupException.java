package com.example.SplitLoop.group.exception;


import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UserNotInGroupException extends BusinessException {

    public UserNotInGroupException(UUID userId, UUID groupId) {
        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.USER_NOT_IN_GROUP,
                String.format(
                        "El usuario %s no pertenece al grupo %s",
                        userId,
                        groupId
                )
        );
    }

}
