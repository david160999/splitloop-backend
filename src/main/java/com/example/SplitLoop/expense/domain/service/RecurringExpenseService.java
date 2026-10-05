package com.example.SplitLoop.expense.domain.service;

import com.example.SplitLoop.common.domain.exception.InvalidDomainArgumentException;
import com.example.SplitLoop.expense.domain.exception.RecurringExpenseInactiveException;
import com.example.SplitLoop.expense.domain.model.*;
import com.example.SplitLoop.expense.domain.policy.ExpenseSplitPolicy;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseParticipantRepository;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.group.domain.model.GroupMember;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RecurringExpenseService {

    public static final int GENERATION_HORIZON_MONTHS = 3;

    private final RecurringExpenseRepository recurringExpenseRepository;
    private final RecurringExpenseParticipantRepository participantRepository;

    private final ExpenseOccurrenceService occurrenceService;

    public RecurringExpenseService(RecurringExpenseRepository recurringExpenseRepository, RecurringExpenseParticipantRepository participantRepository, ExpenseOccurrenceService occurrenceService) {
        this.recurringExpenseRepository = recurringExpenseRepository;
        this.participantRepository = participantRepository;
        this.occurrenceService = occurrenceService;
    }


    public RecurringExpense updateRecurringExpense(
            RecurringExpense recurringExpense,
            List<RecurringExpenseParticipant> participants,
            List<GroupMember> groupMember) {

        ExpenseSplitPolicy.validateParticipants(groupMember, participants);
        ExpenseSplitPolicy.validateSplitConfiguration(recurringExpense, participants);

        // 2. Vincular participantes al gasto mediante inmutabilidad (toBuilder)
        List<RecurringExpenseParticipant> updatedParticipants = participants.stream()
                .map(p -> p.withRecurringExpenseId(recurringExpense.id()))
                .toList();

        // 3. Crear el agregado actualizado
        RecurringExpense expenseToSave = recurringExpense.toBuilder()
                .participants(updatedParticipants)
                .updatedAt(LocalDateTime.now())
                .build();

        // 4. Guardar todo el Agregado Raíz (el adaptador maneja el delete/save de participantes)
        RecurringExpense savedExpense = recurringExpenseRepository.save(expenseToSave);

        // 5. Sincronizar ocurrencias futuras
        occurrenceService.updateFutureOccurrences(savedExpense);

        return savedExpense;
    }



    public void completeIfExpired(RecurringExpense recurringExpense, LocalDate today) {

        if (recurringExpense.endDate() != null && today.isAfter(recurringExpense.endDate())) {

            recurringExpense.complete();
            recurringExpenseRepository.save(recurringExpense);
        }
    }

    public void validateSplitConfiguration(
            SplitType splitType,
            Money totalAmount,
            List<RecurringExpenseParticipant> participants) {

        switch (splitType) {
            case EQUAL -> { /* Sin acción */ }

            case PERCENTAGE -> {
                BigDecimal totalPercentage = participants.stream()
                        .map(RecurringExpenseParticipant::value)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                if (totalPercentage.compareTo(BigDecimal.valueOf(100)) != 0) {
                    throw new InvalidDomainArgumentException("Percentages must sum 100.");
                }
            }

            case FIXED -> {
                BigDecimal totalFixedAmount = participants.stream()
                        .map(RecurringExpenseParticipant::value)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                if (totalFixedAmount.compareTo(totalAmount.amount()) != 0) {
                    throw new InvalidDomainArgumentException("Fixed amounts must equal expense amount.");
                }
            }
        }
    }
}
