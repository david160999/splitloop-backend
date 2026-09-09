package com.example.SplitLoop.payment.controller.query;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetPaymentsByOccurrenceQuery {

    @NotNull
    private UUID occurrenceId;
}
