package com.example.SplitLoop.expense.domain.model;

import com.example.SplitLoop.user.domain.model.User;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Builder(toBuilder = true)
public record ExpenseOccurrenceSplit(
        UUID id,
        ExpenseOccurrence occurrence,
        User user,
        BigDecimal amountOwed,
        BigDecimal amountPaid,
        ExpenseOccurrenceSplitStatus status
) {
    public ExpenseOccurrenceSplit {
        Objects.requireNonNull(occurrence, "La ocurrencia es obligatoria");
        Objects.requireNonNull(user, "El usuario es obligatorio");
        Objects.requireNonNull(amountOwed, "El monto adeudado es obligatorio");

        if (amountPaid == null) {
            amountPaid = BigDecimal.ZERO;
        }
    }

    public BigDecimal getPendingAmount() {
        return amountOwed.subtract(amountPaid);
    }

    public boolean isFullyPaid() {
        return amountPaid.compareTo(amountOwed) >= 0;
    }

    /**
     * Evalúa y devuelve una nueva versión inmutable del split con el total pagado y el estado actualizados.
     *
     * @param totalPaid Suma total acumulada de pagos para este split.
     * @return Nueva instancia de ExpenseOccurrenceSplit con el estado recalculado.
     */
    public ExpenseOccurrenceSplit calculateStatus(BigDecimal totalPaid) {
        BigDecimal paid = (totalPaid != null) ? totalPaid : BigDecimal.ZERO;
        ExpenseOccurrenceSplitStatus newStatus;

        if (paid.compareTo(this.amountOwed) >= 0) {
            newStatus = ExpenseOccurrenceSplitStatus.PAID;
        } else if (paid.compareTo(BigDecimal.ZERO) > 0) {
            newStatus = ExpenseOccurrenceSplitStatus.PARTIALLY_PAID;
        } else {
            newStatus = ExpenseOccurrenceSplitStatus.PENDING;
        }

        return this.toBuilder()
                .amountPaid(paid)
                .status(newStatus)
                .build();
    }

    /**
     * Calcula la deuda restante para este split.
     */
    public BigDecimal calculateRemainingDebt() {
        BigDecimal paid = (this.amountPaid != null) ? this.amountPaid : BigDecimal.ZERO;
        BigDecimal remaining = this.amountOwed.subtract(paid);
        return remaining.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : remaining;
    }
}
