package com.example.SplitLoop.balance.domain.model;

import com.example.SplitLoop.user.domain.model.User;

import java.math.BigDecimal;
import java.util.Objects;

public record PendingExpenseData(
        User user,          // El usuario deudor/participante en la división
        User paidBy,        // El usuario acreedor que pagó el gasto originalmente
        BigDecimal amountOwed,
        BigDecimal amountPaid
) {
    public PendingExpenseData {
        Objects.requireNonNull(user, "user must not be null");
        Objects.requireNonNull(paidBy, "paidBy must not be null");
        Objects.requireNonNull(amountOwed, "amountOwed must not be null");
        Objects.requireNonNull(amountPaid, "amountPaid must not be null");
    }
}