package com.example.SplitLoop.expense.controller.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public class CreateExpenseSplitRequest {

    @NotNull(message = "Expense id is required")
    private UUID expenseId;

    @NotNull(message = "User id is required")
    private UUID userId;

    @NotNull(message = "Amount owed is required")
    @DecimalMin(value = "0.01", inclusive = false, message = "Amount must be > 0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal amountOwed;
}