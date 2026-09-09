package com.example.SplitLoop.expense.controller.query;

import com.example.SplitLoop.expense.domain.entity.RecurringExpenseStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetRecurringExpensesQuery {

    @NotNull
    private UUID groupId;

    @Builder.Default
    private RecurringExpenseStatus status = RecurringExpenseStatus.ACTIVE;
}