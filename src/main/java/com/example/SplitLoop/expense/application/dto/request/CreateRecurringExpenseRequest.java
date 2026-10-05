package com.example.SplitLoop.expense.application.dto.request;

import com.example.SplitLoop.expense.domain.model.Frequency;
import com.example.SplitLoop.expense.domain.model.SplitType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRecurringExpenseRequest {

    @NotNull
    private UUID groupId;

    @NotBlank
    private String name;

    @Size(max = 1000)
    private String description;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @NotNull
    private Frequency frequency;

    @NotNull
    private SplitType splitType;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull
    private UUID paidById;

    @NotEmpty
    @Valid
    private List<ParticipantRequest> participants;
}
