package com.example.SplitLoop.expense.exception;

import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UserNotMemberOfGroupException extends BusinessException {

    public UserNotMemberOfGroupException(UUID username) {
        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.USER_NOT_MEMBER_OF_GROUP,
                String.format(
                        "El usuario '%s' no pertenece al grupo",
                        username
                )
        );
    }

}
