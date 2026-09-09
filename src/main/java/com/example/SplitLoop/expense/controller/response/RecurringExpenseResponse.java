package com.example.SplitLoop.expense.controller.response;

import com.example.SplitLoop.expense.domain.entity.Frequency;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseStatus;
import com.example.SplitLoop.expense.domain.entity.SplitType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class RecurringExpenseResponse {

    private UUID id;

    private UUID groupId;

    private String name;

    private String description;

    private BigDecimal amount;

    private Frequency frequency;

    private SplitType splitType;

    private LocalDate startDate;

    private LocalDate endDate;

    private RecurringExpenseStatus status;

    private UUID paidBy;

    private UUID createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<ParticipantResponse> participants;

}
