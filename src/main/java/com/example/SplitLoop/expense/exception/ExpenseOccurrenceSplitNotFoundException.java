package com.example.SplitLoop.expense.exception;

import com.example.SplitLoop.common.exception.BusinessException;
import com.example.SplitLoop.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ExpenseOccurrenceSplitNotFoundException extends BusinessException {

    public ExpenseOccurrenceSplitNotFoundException(UUID splitId) {
        super(
                HttpStatus.NOT_FOUND,
                ErrorCode.EXPENSE_OCCURRENCE_SPLIT_NOT_FOUND,
                String.format(
                        "No existe ningún reparto de la ocurrencia de gasto con id %s",
                        splitId
                )
        );
    }

}