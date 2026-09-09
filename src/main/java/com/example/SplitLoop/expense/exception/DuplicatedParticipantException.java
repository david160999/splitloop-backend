package com.example.SplitLoop.expense.exception;


import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class DuplicatedParticipantException extends BusinessException {

    public DuplicatedParticipantException() {
        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.DUPLICATED_PARTICIPANT,
                "La lista de participantes contiene usuarios duplicados"
        );
    }

}