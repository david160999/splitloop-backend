package com.example.SplitLoop.expense.domain.exception;


import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class ParticipantsCannotBeEmptyException extends BusinessException {

    public ParticipantsCannotBeEmptyException() {
        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.PARTICIPANTS_CANNOT_BE_EMPTY,
                "La lista de participantes no puede estar vacía"
        );
    }

}
