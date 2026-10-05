package com.example.SplitLoop.expense.domain.model;

import com.example.SplitLoop.common.domain.exception.InvalidDomainArgumentException;
import com.example.SplitLoop.expense.domain.exception.CannotModifyPaidOccurrenceException;
import com.example.SplitLoop.expense.domain.exception.CannotModifyPartiallyPaidOccurrenceException;
import com.example.SplitLoop.expense.domain.exception.OccurrenceAlreadyCancelledException;
import com.example.SplitLoop.expense.domain.exception.OccurrenceNotCancelledException;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.user.domain.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Builder(toBuilder = true)
public record ExpenseOccurrence(
        UUID id,
        RecurringExpense recurringExpense,
        Group group,
        String name,
        Money amount,
        User paidBy,
        LocalDate dueDate,
        LocalDate periodStart,
        LocalDate periodEnd,
        ExpenseOccurrenceStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    // =========================================================================
    // 1. CONSTRUCTOR COMPACTO & INVARIANTES DE CREACIÓN
    // =========================================================================

    public ExpenseOccurrence {
        Objects.requireNonNull(recurringExpense, "El gasto recurrente padre es obligatorio");
        Objects.requireNonNull(group, "El grupo no puede ser nulo");
        Objects.requireNonNull(name, "El nombre de la ocurrencia es obligatorio");
        Objects.requireNonNull(amount, "El monto no puede ser nulo");
        Objects.requireNonNull(paidBy, "El pagador es obligatorio");
        Objects.requireNonNull(dueDate, "La fecha de vencimiento es obligatoria");
        Objects.requireNonNull(periodStart, "La fecha de inicio del período es obligatoria");
        Objects.requireNonNull(periodEnd, "La fecha de fin del período es obligatoria");

        createdAt = (createdAt != null) ? createdAt : LocalDateTime.now();
        updatedAt = (updatedAt != null) ? updatedAt : createdAt;
    }

    // =========================================================================
    // 2. MÉTODOS DE TRANSICIÓN DE ESTADO (Comportamiento de Dominio principal)
    // =========================================================================

    public ExpenseOccurrence cancel() {
        ensureCanBeCancelled();

        return this.toBuilder()
                .status(ExpenseOccurrenceStatus.CANCELLED)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public ExpenseOccurrence markAsPending() {
        ensureCanBeReopened();

        return this.toBuilder()
                .status(ExpenseOccurrenceStatus.PENDING)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public ExpenseOccurrence markAsPaid() {
        ensureCanBePaid();

        return this.toBuilder()
                .status(ExpenseOccurrenceStatus.PAID)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public ExpenseOccurrence markAsPartiallyPaid() {
        ensureCanBeModified();
        return this.toBuilder()
                .status(ExpenseOccurrenceStatus.PARTIALLY_PAID)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // =========================================================================
    // 3. MUTACIONES Y CAMBIOS DE PROPIEDADES INMUTABLES
    // =========================================================================

    public ExpenseOccurrence updateDetails(String newName, Money newAmount, LocalDate newDueDate, User newPaidBy) {

        ensureCanBeModified();

        // 2. Si se especifica un pagador y es DIFERENTE al actual, aplicamos la regla estricta de cambio
        ExpenseOccurrence target = (newPaidBy != null)
                ? this.changePaidBy(newPaidBy)
                : this;

        return target.toBuilder()
                .name(newName != null ? newName : this.name)
                .amount(newAmount != null ? newAmount : this.amount)
                .dueDate(newDueDate != null ? newDueDate : this.dueDate)
                .updatedAt(LocalDateTime.now())
                .build();
    }


    public ExpenseOccurrence changePaidBy(User newPaidBy) {
        if (this.paidBy.id().equals(newPaidBy.id())) {
            throw new InvalidDomainArgumentException("El nuevo pagador no puede ser la misma persona que el pagador actual.");
        }

        ensureCanBeModified();
        return this.toBuilder()
                .paidBy(newPaidBy)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Recalcula el estado global a partir de los splits de la ocurrencia.
     *
     * @param splits Lista de splits leídos de la BD.
     * @return Nueva instancia inmutable con el estado recalculado.
     */
    public ExpenseOccurrence recalculateStatus(List<ExpenseOccurrenceSplit> splits) {
        if (splits == null || splits.isEmpty()) {
            return this;
        }

        ExpenseOccurrenceStatus newStatus = determineStatusFromSplits(splits);

        return this.toBuilder()
                .status(newStatus)
                .build();
    }


    private ExpenseOccurrenceStatus determineStatusFromSplits(List<ExpenseOccurrenceSplit> splits) {
        boolean allPaid = splits.stream()
                .allMatch(s -> s.status() == ExpenseOccurrenceSplitStatus.PAID);

        if (allPaid) {
            return ExpenseOccurrenceStatus.PAID;
        }

        boolean anyPaidOrPartial = splits.stream()
                .anyMatch(s -> s.status() == ExpenseOccurrenceSplitStatus.PAID
                        || s.status() == ExpenseOccurrenceSplitStatus.PARTIALLY_PAID);

        if (anyPaidOrPartial) {
            return ExpenseOccurrenceStatus.PARTIALLY_PAID;
        }

        return ExpenseOccurrenceStatus.PENDING;
    }

    // =========================================================================
    // 4. CONSULTAS Y PREDICADOS (Lógica de preguntas booleanas)
    // =========================================================================

    public boolean isCancelled() {
        return ExpenseOccurrenceStatus.CANCELLED.equals(this.status);
    }

    public boolean isPaid() {
        return ExpenseOccurrenceStatus.PAID.equals(this.status);
    }

    public boolean isPartiallyPaid() {
        return ExpenseOccurrenceStatus.PARTIALLY_PAID.equals(this.status);
    }


    // =========================================================================
    // 5. MÉTODOS DE GUARDA / VALIDACIÓN PRIVADOS (Guard Clauses)
    // =========================================================================
    public void ensureCanBeModified() {
        if (this.status == ExpenseOccurrenceStatus.PAID) {
            throw new CannotModifyPaidOccurrenceException(this.id);
        }
        if (this.status == ExpenseOccurrenceStatus.PARTIALLY_PAID) {
            throw new CannotModifyPartiallyPaidOccurrenceException();
        }

    }

    public void ensureCanBeCancelled() {
        if (this.status == ExpenseOccurrenceStatus.CANCELLED) {
            throw new OccurrenceAlreadyCancelledException();
        }
        ensureCanBeModified();
    }

    public void ensureCanBeReopened() {
        if (this.status != ExpenseOccurrenceStatus.CANCELLED) {
            throw new OccurrenceNotCancelledException(this.id);
        }
    }

    public void ensureCanBePaid() {
        if (isCancelled()) {
            throw new CannotModifyPaidOccurrenceException(this.id); // O tu excepción específica: OccurrenceCancelledException
        }
        if (isPaid()) {
            throw new IllegalStateException("La ocurrencia ya se encuentra pagada.");
        }
    }


}
