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
public class GenerateOccurrenceCommand {

    @NotNull
    private UUID recurringExpenseId;

    @NotNull
    private LocalDate dueDate;
}