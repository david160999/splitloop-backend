package com.example.SplitLoop.payment.domain.model;

import com.example.SplitLoop.expense.domain.model.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceSplit;
import com.example.SplitLoop.user.domain.model.User;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Builder(toBuilder = true)
public record Payment(
        UUID id,
        ExpenseOccurrence occurrence,
        ExpenseOccurrenceSplit split,
        User fromUser,
        User toUser,
        BigDecimal amount,
        String note,
        LocalDateTime paidAt,
        User createdBy,
        PaymentType type
) {
    // Constructor compacto para aplicar defaults y validar invariantes
    public Payment {
        Objects.requireNonNull(occurrence, "La ocurrencia del gasto no puede ser nula");
        Objects.requireNonNull(fromUser, "El usuario emisor no puede ser nulo");
        Objects.requireNonNull(toUser, "El usuario receptor no puede ser nulo");
        Objects.requireNonNull(amount, "El monto no puede ser nulo");
        Objects.requireNonNull(createdBy, "El creador no puede ser nulo");

        if (paidAt == null) {
            paidAt = LocalDateTime.now();
        }

        if (type == null) {
            type = PaymentType.PAYMENT;
        }
    }

    public boolean isPayer(UUID userId) {
        return fromUser != null && fromUser.id().equals(userId);
    }

    public boolean isReceiver(UUID userId) {
        return toUser != null && toUser.id().equals(userId);
    }
}