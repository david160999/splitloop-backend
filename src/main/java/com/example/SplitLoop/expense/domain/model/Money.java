package com.example.SplitLoop.expense.domain.model;

import com.example.SplitLoop.common.domain.exception.InvalidDomainArgumentException;

import java.math.BigDecimal;

public record Money(BigDecimal amount) {
    public Money {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDomainArgumentException("Invalid amount.");
        }
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }
}
