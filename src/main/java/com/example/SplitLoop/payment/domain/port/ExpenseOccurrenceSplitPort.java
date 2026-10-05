package com.example.SplitLoop.payment.domain.port;

import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceSplit;

import java.util.Optional;
import java.util.UUID;

public interface ExpenseOccurrenceSplitPort {

    /**
     * Recalcula y actualiza el saldo pendiente y el estado individual del split
     * asignado a un usuario concreto dentro de la ocurrencia del gasto.
     */
    void updateStatusAfterPayment(UUID splitId);

    Optional<ExpenseOccurrenceSplit> findById(UUID splitId);
}
