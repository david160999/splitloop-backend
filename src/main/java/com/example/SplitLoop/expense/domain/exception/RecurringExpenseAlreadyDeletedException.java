package com.example.SplitLoop.expense.domain.exception;

import com.example.SplitLoop.common.domain.exception.BusinessException;
import com.example.SplitLoop.common.domain.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class RecurringExpenseAlreadyDeletedException extends BusinessException {

    public RecurringExpenseAlreadyDeletedException(UUID id) {
        super(
                HttpStatus.CONFLICT,
                ErrorCode.RECURRING_EXPENSE_ALREADY_DELETED,
                String.format("El gasto recurrente con ID '%s' ya ha sido eliminado", id)
        );
    }
}
