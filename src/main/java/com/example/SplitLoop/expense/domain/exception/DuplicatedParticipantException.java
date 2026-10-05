package com.example.SplitLoop.expense.domain.exception;


import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
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