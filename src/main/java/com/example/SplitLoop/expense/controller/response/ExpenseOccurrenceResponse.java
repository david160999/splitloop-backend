package com.example.SplitLoop.expense.controller.response;

import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseOccurrenceResponse {

    private UUID id;

    private UUID recurringExpenseId;

    private UUID groupId;

    private String name;

    private BigDecimal amount;

    private UUID paidBy;

    private LocalDate dueDate;

    private LocalDate periodStart;

    private LocalDate periodEnd;

    private ExpenseOccurrenceStatus status;

    private LocalDateTime createdAt;

    private List<ExpenseOccurrenceSplitResponse> splits;
}
