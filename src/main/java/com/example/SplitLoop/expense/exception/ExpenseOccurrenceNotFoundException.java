package com.example.SplitLoop.expense.exception;


import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ExpenseOccurrenceNotFoundException extends BusinessException {

    public ExpenseOccurrenceNotFoundException(UUID expenseOccurrenceId) {
        super(
                HttpStatus.NOT_FOUND,
                ErrorCode.EXPENSE_OCCURRENCE_NOT_FOUND,
                String.format(
                        "No existe ninguna ocurrencia de gasto con id %s",
                        expenseOccurrenceId
                )
        );
    }

}
