package com.example.SplitLoop.expense.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class RecurringExpenseAlreadyCompletedException extends BusinessException {

    public RecurringExpenseAlreadyCompletedException(UUID id) {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.RECURRING_EXPENSE_ALREADY_COMPLETED,
                String.format("El gasto recurrente con ID '%s' ya se encuentra completado", id)
        );
    }
}