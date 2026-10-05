package com.example.SplitLoop.balance.domain.model;

import com.example.SplitLoop.user.domain.model.User;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Builder(toBuilder = true)
public record Debt(
        User debtor,
        User creditor,
        BigDecimal amount
) {
    public Debt {
        Objects.requireNonNull(debtor, "El deudor no puede ser nulo");
        Objects.requireNonNull(creditor, "El acreedor no puede ser nulo");
        Objects.requireNonNull(amount, "El monto no puede ser nulo");

        if (debtor.equals(creditor)) {
            throw new IllegalArgumentException("El deudor y el acreedor no pueden ser el mismo usuario");
        }
    }

    public boolean isDebtor(UUID userId) {
        return debtor != null && debtor.id().equals(userId);
    }

    public boolean isCreditor(UUID userId) {
        return creditor != null && creditor.id().equals(userId);
    }
}
