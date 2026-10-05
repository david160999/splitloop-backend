package com.example.SplitLoop.expense.domain.model;

import com.example.SplitLoop.common.domain.exception.InvalidDomainArgumentException;
import com.example.SplitLoop.expense.domain.exception.*;
import com.example.SplitLoop.group.domain.model.Group;
import com.example.SplitLoop.user.domain.model.User;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Builder(toBuilder = true)
public record RecurringExpense(
        UUID id,
        Group group,
        String name,
        String description,
        Money amount,
        Frequency frequency,
        SplitType splitType,
        LocalDate startDate,
        LocalDate endDate,
        RecurringExpenseStatus status,
        User paidBy,
        User createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<RecurringExpenseParticipant> participants
) {

    // =========================================================================
    // 1. CONSTRUCTOR COMPACTO & INVARIANTES DE CREACIÓN
    // =========================================================================
    public RecurringExpense {
        Objects.requireNonNull(group, "El grupo no puede ser nulo");
        Objects.requireNonNull(name, "El nombre del gasto recurrente es obligatorio");
        Objects.requireNonNull(amount, "El monto no puede ser nulo");
        Objects.requireNonNull(frequency, "La frecuencia es obligatoria");
        Objects.requireNonNull(startDate, "La fecha de inicio es obligatoria");
        Objects.requireNonNull(paidBy, "El usuario pagador es obligatorio");
        Objects.requireNonNull(createdBy, "El creador es obligatorio");

        participants = (participants == null) ? Collections.emptyList() : List.copyOf(participants);

        createdAt = (createdAt != null) ? createdAt : LocalDateTime.now();
        updatedAt = (updatedAt != null) ? updatedAt : createdAt;

        ensureValidDates(startDate, endDate);
    }

    // =========================================================================
    // 2. MÉTODOS DE TRANSICIÓN DE ESTADO (Comportamiento de Dominio Principal)
    // =========================================================================
    public RecurringExpense pause() {
        ensureCanBePaused();
        return this.toBuilder()
                .status(RecurringExpenseStatus.PAUSED)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public RecurringExpense resume() {
        ensureCanBeResumed();
        return this.toBuilder()
                .status(RecurringExpenseStatus.ACTIVE)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public RecurringExpense complete() {
        ensureCanBeCompleted();
        return this.toBuilder()
                .status(RecurringExpenseStatus.COMPLETED)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public RecurringExpense delete() {
        ensureCanBeDeleted();
        return this.toBuilder()
                .status(RecurringExpenseStatus.DELETED)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // =========================================================================
    // 3. MUTACIONES Y OPERACIONES DE AGREGADO
    // =========================================================================
    public RecurringExpense duplicate(User newCreatedBy, LocalDate newStartDate) {
        LocalDateTime now = LocalDateTime.now();

        List<RecurringExpenseParticipant> duplicatedParticipants = this.participants.stream()
                .map(RecurringExpenseParticipant::prepareForDuplication)
                .toList();

        return this.toBuilder()
                .id(UUID.randomUUID())
                .status(RecurringExpenseStatus.ACTIVE)
                .createdBy(newCreatedBy)
                .startDate(newStartDate != null ? newStartDate : LocalDate.now())
                .createdAt(now)
                .updatedAt(now)
                .participants(duplicatedParticipants)
                .build();
    }

    public RecurringExpense update(
            String newName,
            String newDescription,
            Money newAmount,
            Frequency newFrequency,
            SplitType newSplitType,
            LocalDate newStartDate,
            LocalDate newEndDate,
            User newPaidBy,
            List<RecurringExpenseParticipant> newParticipants
    ) {
        ensureCanBeModified();

        return this.toBuilder()
                .name(newName != null ? newName : this.name)
                .description(newDescription != null ? newDescription : this.description)
                .amount(newAmount != null ? newAmount : this.amount)
                .frequency(newFrequency != null ? newFrequency : this.frequency)
                .splitType(newSplitType != null ? newSplitType : this.splitType)
                .startDate(newStartDate != null ? newStartDate : this.startDate)
                .endDate(newEndDate != null ? newEndDate : this.endDate)
                .paidBy(newPaidBy != null ? newPaidBy : this.paidBy)
                .participants(newParticipants != null ? List.copyOf(newParticipants) : this.participants)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // =========================================================================
    // 4. CONSULTAS Y PREDICADOS (Lógica de preguntas booleanas)
    // =========================================================================
    public boolean isActive() {
        return RecurringExpenseStatus.ACTIVE.equals(this.status);
    }

    public boolean isPaused() {
        return RecurringExpenseStatus.PAUSED.equals(this.status);
    }

    public boolean isCompleted() {
        return RecurringExpenseStatus.COMPLETED.equals(this.status);
    }

    public boolean isDeleted() {
        return RecurringExpenseStatus.DELETED.equals(this.status);
    }

    // =========================================================================
    // 5. MÉTODOS DE GUARDA Y VALIDACIÓN PRIVADOS (Guard Clauses / SonarQube)
    // =========================================================================
    public void ensureCanBePaused() {
        if (!isActive()) {
            throw new RecurringExpenseAlreadyPausedException(this.id);
        }
    }

    public void ensureCanBeResumed() {
        if (isActive()) {
            throw new RecurringExpenseAlreadyActiveException(this.id);
        }
    }

    public void ensureCanBeCompleted() {
        if (isCompleted()) {
            throw new RecurringExpenseAlreadyCompletedException(this.id);
        }
        if (isDeleted()) {
            throw new InvalidRecurringExpenseStateException("Cannot complete a deleted recurring expense", this.id);
        }
    }

    public void ensureCanBeDeleted() {
        ensureCanBePaused();
        if (isDeleted()) {
            throw new RecurringExpenseAlreadyDeletedException(this.id);
        }
    }

    public void ensureCanBeModified() {
        if (isDeleted()) {
            throw new InvalidRecurringExpenseStateException("No se puede modificar un gasto recurrente eliminado", this.id);
        }
        if (isCompleted()) {
            throw new InvalidRecurringExpenseStateException("No se puede modificar un gasto recurrente completado", this.id);
        }
    }

    private static void ensureValidDates(LocalDate startDate, LocalDate endDate) {
        if (endDate != null && startDate.isAfter(endDate)) {
            throw new InvalidDomainArgumentException("Invalid dates.");
        }
    }
}