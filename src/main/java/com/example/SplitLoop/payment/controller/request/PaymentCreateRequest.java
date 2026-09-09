package com.example.SplitLoop.payment.controller.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public class PaymentCreateRequest {

    @NotNull(message = "Group is required")
    private UUID groupId;

    @NotNull(message = "From user is required")
    private UUID fromUserId;

    @NotNull(message = "To user is required")
    private UUID toUserId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
}
