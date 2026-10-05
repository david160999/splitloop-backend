package com.example.SplitLoop.expense.domain.exception;


import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class RecurringExpenseInactiveException extends BusinessException {

    public RecurringExpenseInactiveException() {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.RECURRING_EXPENSE_INACTIVE,
                "El gasto recurrente está inactivo"
        );
    }

}
