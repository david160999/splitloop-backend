package com.example.SplitLoop.balance.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class GroupHasActiveExpensesException extends BusinessException {

    public GroupHasActiveExpensesException() {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.GROUP_HAS_ACTIVE_EXPENSES,
                "No se puede realizar la operación porque el grupo tiene gastos activos"
        );
    }

}