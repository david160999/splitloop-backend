package com.example.SplitLoop.balance.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
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