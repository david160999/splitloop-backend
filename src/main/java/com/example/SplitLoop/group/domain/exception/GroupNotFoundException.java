package com.example.SplitLoop.group.domain.exception;


import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import java.util.UUID;

public class GroupNotFoundException extends BusinessException {

    public GroupNotFoundException(UUID groupId) {
        super(
                HttpStatus.NOT_FOUND,
                ErrorCode.GROUP_NOT_FOUND,
                String.format(
                        "No existe ningún grupo con id %s",
                        groupId
                )
        );
    }

}
