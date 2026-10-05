package com.example.SplitLoop.expense.domain.model;

import com.example.SplitLoop.user.domain.model.User;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Builder(toBuilder = true)
public record RecurringExpenseParticipant(
        UUID id,
        UUID recurringExpenseId,
        User user,
        BigDecimal value
) {
    public RecurringExpenseParticipant {
        Objects.requireNonNull(user, "El usuario participante es obligatorio");
    }

    public boolean isUser(UUID userId) {
        return user != null && user.id().equals(userId);
    }

    public RecurringExpenseParticipant prepareForDuplication() {
        return this.toBuilder()
                .id(UUID.randomUUID()) // Asigna un nuevo ID para no sobreescribir el participante original
                .build();
    }

    /**
     */
    public RecurringExpenseParticipant withRecurringExpenseId(UUID recurringExpenseId) {
        return this.toBuilder()
                .recurringExpenseId(recurringExpenseId)
                .build();
    }
}
