package com.example.SplitLoop.payment.domain.port;

import com.example.SplitLoop.expense.domain.model.ExpenseOccurrence;

import java.util.Optional;
import java.util.UUID;

public interface ExpenseOccurrencePort {

    /**
     * Recalcula y actualiza el estado de la ocurrencia del gasto
     * (e.g. PENDING, PARTIALLY_PAID, PAID) en función de los pagos realizados.
     */
    void updateStatusAfterPayment(UUID occurrenceId);

    Optional<ExpenseOccurrence> findById(UUID occurrenceId);
}
