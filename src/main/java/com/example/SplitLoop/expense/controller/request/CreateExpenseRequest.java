package com.example.SplitLoop.expense.controller.request;

import jakarta.validation.constraints.*;
import jdk.jfr.Frequency;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class CreateExpenseRequest {

    @NotBlank(message = "Expense name is required")
    private String name;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be > 0")
    private BigDecimal amount;

    @NotNull(message = "Group is required")
    private UUID groupId;

    @NotNull(message = "CreatedBy is required")
    private UUID createdBy;

    @NotNull
    private Frequency frequency;

    @NotEmpty
    private List<CreateExpenseSplitRequest> splits;
}