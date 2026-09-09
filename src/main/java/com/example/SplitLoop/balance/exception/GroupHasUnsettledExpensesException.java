package com.example.SplitLoop.balance.exception;

import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class GroupHasUnsettledExpensesException extends BusinessException {

    public GroupHasUnsettledExpensesException() {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.GROUP_HAS_UNSETTLED_EXPENSES,
                "No se puede realizar la operación porque el grupo tiene gastos sin liquidar"
        );
    }

}