package com.example.SplitLoop.group.domain.port;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface BalanceClientPort {
    // Definimos el método con los parámetros explícitos
    BigDecimal getPendingAmountByGroupId(UUID groupId, LocalDate from, LocalDate to);

    // Método de conveniencia sobrecargado para cuando no hay rango de fechas
    default BigDecimal getPendingAmountByGroupId(UUID groupId) {
        return getPendingAmountByGroupId(groupId, null, null);
    }
}
