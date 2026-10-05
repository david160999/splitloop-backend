package com.example.SplitLoop.payment.domain.model;

import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.user.domain.model.User;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Builder(toBuilder = true)
public record PaymentObligation(
        UUID id,
        Group group,
        User debtor,
        User creditor,
        BigDecimal amount,
        PaymentType status,
        LocalDateTime createdAt,
        LocalDateTime paidAt
) {
    // Constructor compacto para validar invariantes y asignación de timestamps por defecto
    public PaymentObligation {
        Objects.requireNonNull(group, "El grupo no puede ser nulo");
        Objects.requireNonNull(debtor, "El deudor no puede ser nulo");
        Objects.requireNonNull(creditor, "El acreedor no puede ser nulo");
        Objects.requireNonNull(amount, "El monto no puede ser nulo");
        Objects.requireNonNull(status, "El estado no puede ser nulo");

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public boolean isDebtor(UUID userId) {
        return debtor != null && debtor.id().equals(userId);
    }

    public boolean isCreditor(UUID userId) {
        return creditor != null && creditor.id().equals(userId);
    }

    public boolean isPaid() {
        return paidAt != null;
    }
}