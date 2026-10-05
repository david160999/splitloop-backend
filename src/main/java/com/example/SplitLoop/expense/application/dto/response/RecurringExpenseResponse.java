package com.example.SplitLoop.expense.application.dto.response;

import com.example.SplitLoop.expense.domain.model.Frequency;
import com.example.SplitLoop.expense.domain.model.RecurringExpenseStatus;
import com.example.SplitLoop.expense.domain.model.SplitType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Builder;

@Builder
public record RecurringExpenseResponse(
        UUID id,
        UUID groupId,
        String name,
        String description,
        BigDecimal amount,
        Frequency frequency,
        SplitType splitType,
        LocalDate startDate,
        LocalDate endDate,
        RecurringExpenseStatus status,
        UUID paidBy,
        UUID createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<ParticipantResponse> participants
) {}
