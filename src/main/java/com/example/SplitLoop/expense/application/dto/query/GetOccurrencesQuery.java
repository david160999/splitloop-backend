package com.example.SplitLoop.expense.application.dto.query;

import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetOccurrencesQuery {

    @NotNull
    private UUID recurringExpenseId;

    private ExpenseOccurrenceStatus status;

    private LocalDate from;

    private LocalDate to;
}
