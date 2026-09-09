package com.example.SplitLoop.expense.controller.query;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetOccurrenceQuery {

    @NotNull
    private UUID occurrenceId;
}