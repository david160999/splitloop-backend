package com.example.SplitLoop.expense.controller.command;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DuplicateRecurringExpenseCommand {

    @NotNull
    private UUID expenseId;

    @NotNull
    private LocalDate startDate;
}
