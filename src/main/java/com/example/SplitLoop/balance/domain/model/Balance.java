package com.example.SplitLoop.balance.domain.model;


import com.example.SplitLoop.user.domain.model.User;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.Objects;

@Builder(toBuilder = true)
public record Balance(
        User user,
        BigDecimal amount
) {
    public Balance {
        Objects.requireNonNull(user, "El usuario no puede ser nulo");

        if (amount == null) {
            amount = BigDecimal.ZERO;
        }
    }

    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }
}
