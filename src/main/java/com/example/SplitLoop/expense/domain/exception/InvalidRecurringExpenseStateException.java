package com.example.SplitLoop.expense.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class InvalidRecurringExpenseStateException extends BusinessException {

    public InvalidRecurringExpenseStateException(String message, UUID id) {
        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_RECURRING_EXPENSE_STATE,
                String.format("%s (ID: '%s')", message, id)
        );
    }
}