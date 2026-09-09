package com.example.SplitLoop.expense.controller.command;

import com.example.SplitLoop.expense.controller.request.ParticipantRequest;
import com.example.SplitLoop.expense.domain.entity.Frequency;
import com.example.SplitLoop.expense.domain.entity.SplitType;
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
public class UpdateRecurringExpenseCommand {

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
