package com.example.SplitLoop.group.exception;

import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class OnlyGroupCreatorCanDeleteException extends BusinessException {

    public OnlyGroupCreatorCanDeleteException() {
        super(
                HttpStatus.FORBIDDEN,
                ErrorCode.ONLY_GROUP_CREATOR_CAN_DELETE,
                "Solo el creador del grupo puede eliminarlo"
        );
    }

}
