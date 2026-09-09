package com.example.SplitLoop.expense.controller.response;

import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplitStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseOccurrenceSplitResponse {

    private UUID id;

    private UUID userId;

    private BigDecimal amountOwed;

    private BigDecimal amountPaid;

    private ExpenseOccurrenceSplitStatus status;
}