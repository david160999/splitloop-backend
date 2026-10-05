package com.example.SplitLoop.expense.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class SamePaidByException extends BusinessException {

    public SamePaidByException() {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.SAME_PAID_BY,
                "El nuevo pagador debe ser diferente del pagador actual"
        );
    }

}
