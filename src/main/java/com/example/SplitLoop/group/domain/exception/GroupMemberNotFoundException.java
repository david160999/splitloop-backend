package com.example.SplitLoop.group.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class GroupMemberNotFoundException extends BusinessException {

    public GroupMemberNotFoundException(UUID groupId, UUID userId) {
        super(
                HttpStatus.NOT_FOUND,
                ErrorCode.GROUP_MEMBER_NOT_FOUND,
                String.format("El usuario con ID '%s' no pertenece al grupo con ID '%s'", userId, groupId)
        );
    }

    public GroupMemberNotFoundException(String message) {
        super(
                HttpStatus.NOT_FOUND,
                ErrorCode.GROUP_MEMBER_NOT_FOUND,
                message
        );
    }
}