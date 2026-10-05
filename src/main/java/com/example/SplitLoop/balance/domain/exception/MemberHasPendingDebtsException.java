package com.example.SplitLoop.balance.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
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
