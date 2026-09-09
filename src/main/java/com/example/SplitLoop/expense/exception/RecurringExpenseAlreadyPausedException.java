package com.example.SplitLoop.expense.exception;

import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class RecurringExpenseAlreadyPausedException extends BusinessException {

    public RecurringExpenseAlreadyPausedException(UUID recurringExpenseId) {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.RECURRING_EXPENSE_ALREADY_PAUSED,
                String.format(
                        "El gasto recurrente %s ya está pausado",
                        recurringExpenseId
                )
        );
    }

}
