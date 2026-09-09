package com.example.SplitLoop.expense.controller.command;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PauseRecurringExpenseCommand {

    @NotNull
    private UUID expenseId;
}