package com.example.SplitLoop.expense.exception;


import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class RecurringExpenseNotFoundException extends BusinessException {

    public RecurringExpenseNotFoundException(UUID recurringExpenseId) {
        super(
                HttpStatus.NOT_FOUND,
                ErrorCode.RECURRING_EXPENSE_NOT_FOUND,
                String.format(
                        "No existe ningún gasto recurrente con id %s",
                        recurringExpenseId
                )
        );
    }

}