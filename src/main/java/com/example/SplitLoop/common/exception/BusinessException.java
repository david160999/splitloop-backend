package com.example.SplitLoop.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorCode errorCode;

    protected BusinessException(
            HttpStatus status,
            ErrorCode errorCode,
            String message) {

        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

}