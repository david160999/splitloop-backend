package com.example.SplitLoop.balance.exception;

import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class MemberHasPendingDebtsException extends BusinessException {

    public MemberHasPendingDebtsException() {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.MEMBER_HAS_PENDING_DEBTS,
                "El miembro tiene deudas pendientes y no puede realizar esta acción"
        );
    }

}
