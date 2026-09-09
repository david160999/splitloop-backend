package com.example.SplitLoop.expense.controller.request;

import jakarta.validation.constraints.*;
import jdk.jfr.Frequency;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class UpdateExpenseRequest {

    @NotBlank(message = "Expense name is required")
    @Size(min = 3, max = 100, message = "Expense name must be between 3 and 100 characters")
    private String name;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Frequency is required")
    private Frequency frequency;

    @NotEmpty(message = "At least one participant is required")
    private List<CreateExpenseSplitRequest> splits;
}
