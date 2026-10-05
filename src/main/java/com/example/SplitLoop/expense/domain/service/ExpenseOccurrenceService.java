package com.example.SplitLoop.expense.domain.service;

import com.example.SplitLoop.expense.domain.model.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceStatus;
import com.example.SplitLoop.expense.domain.model.Frequency;
import com.example.SplitLoop.expense.domain.model.RecurringExpense;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ExpenseOccurrenceService {

    private final ExpenseOccurrenceRepository occurrenceRepository;
    private final ExpenseOccurrenceSplitService splitService;

    public ExpenseOccurrenceService(ExpenseOccurrenceRepository occurrenceRepository, ExpenseOccurrenceSplitService splitService) {
        this.occurrenceRepository = occurrenceRepository;
        this.splitService = splitService;
    }


    public void updateFutureOccurrences(RecurringExpense recurringExpense) {

        List<ExpenseOccurrence> futureOccurrences = occurrenceRepository.findFutureOccurrences(recurringExpense.id(), LocalDate.now());

        List<ExpenseOccurrence> updatedOccurrences = futureOccurrences.stream()
                .map(occurrence -> {
                    // 1. Validar reglas de dominio
                    occurrence.ensureCanBeModified();

                    // 2. Crear nueva instancia inmutable actualizada
                    ExpenseOccurrence updated = occurrence.toBuilder()
                            .name(recurringExpense.name())
                            .amount(recurringExpense.amount())
                            .paidBy(recurringExpense.paidBy())
                            .updatedAt(LocalDateTime.now())
                            .build();

                    // 3. Recalcular los splits
                    splitService.recalculateParticipants(updated);

                    return updated;
                })
                .toList();

        occurrenceRepository.saveAll(updatedOccurrences);
    }

    public void cancelFutureOccurrences(RecurringExpense recurringExpense) {

        List<ExpenseOccurrence> futureOccurrences =
                occurrenceRepository.findFutureOccurrences(
                        recurringExpense.id(),
                        LocalDate.now());

        futureOccurrences.forEach(ExpenseOccurrence::cancel);

        occurrenceRepository.saveAll(futureOccurrences);
    }

    public List<ExpenseOccurrence> generatePendingOccurrences(RecurringExpense recurringExpense, LocalDate until) {

        List<ExpenseOccurrence> generated = new ArrayList<>();

        LocalDate nextDueDate = occurrenceRepository
                .findTopByRecurringExpenseIdOrderByDueDateDesc(recurringExpense.id())
                .map(last -> nextOccurrenceDate(
                        last.dueDate(),
                        recurringExpense.frequency()))
                .orElse(recurringExpense.startDate());

        while (!nextDueDate.isAfter(until)) {

            generated.add(
                    createOccurrence(
                            recurringExpense,
                            nextDueDate));

            nextDueDate = nextOccurrenceDate(
                    nextDueDate,
                    recurringExpense.frequency());
        }

        return generated;
    }

    private ExpenseOccurrence createOccurrence(RecurringExpense recurringExpense, LocalDate dueDate) {

        ExpenseOccurrence occurrence = ExpenseOccurrence.builder()
                .recurringExpense(recurringExpense)
                .group(recurringExpense.group())
                .name(recurringExpense.name())
                .amount(recurringExpense.amount())
                .paidBy(recurringExpense.paidBy())
                .dueDate(dueDate)
                .periodStart(dueDate)
                .periodEnd(calculatePeriodEnd(
                        dueDate,
                        recurringExpense.frequency()))
                .status(ExpenseOccurrenceStatus.PENDING)
                .build();

        ExpenseOccurrence saved = occurrenceRepository.save(occurrence);

        splitService.recalculateParticipants(saved);

        return saved;
    }

    private LocalDate nextOccurrenceDate(LocalDate current, Frequency frequency) {

        return switch (frequency) {

            case DAILY -> current.plusDays(1);

            case WEEKLY -> current.plusWeeks(1);

            case MONTHLY -> current.plusMonths(1);

            case YEARLY -> current.plusYears(1);
        };
    }

    private LocalDate calculatePeriodEnd(LocalDate start, Frequency frequency) {

        return switch (frequency) {

            case DAILY -> start;

            case WEEKLY -> start.plusDays(6);

            case MONTHLY -> start.plusMonths(1).minusDays(1);

            case YEARLY -> start.plusYears(1).minusDays(1);
        };
    }
}