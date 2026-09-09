package com.example.SplitLoop.expense.exception;

import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
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
