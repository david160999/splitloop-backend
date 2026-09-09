package com.example.SplitLoop.expense.domain.service;

import com.example.SplitLoop.expense.domain.entity.*;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.expense.domain.validator.ExpenseValidator;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseOccurrenceServiceImpl implements ExpenseOccurrenceService {

    private final ExpenseOccurrenceRepository occurrenceRepository;

    private final ExpenseOccurrenceSplitService splitService;

    private final ExpenseValidator validator;

    @Override
    public ExpenseOccurrence updateOccurrence(ExpenseOccurrence occurrence) {

        validator.validateOccurrence(occurrence);

        ExpenseOccurrence saved = occurrenceRepository.save(occurrence);

        splitService.recalculateParticipants(saved);

        return saved;
    }

    @Override
    public void cancelOccurrence(ExpenseOccurrence occurrence, GroupMember member) {

        validator.validateCanCancel(occurrence, member);

        occurrence.setStatus(ExpenseOccurrenceStatus.CANCELLED);

        occurrenceRepository.save(occurrence);
    }

    @Override
    public void reopen(ExpenseOccurrence occurrence, GroupMember member) {

        validator.validateCanReopen(occurrence, member);

        occurrence.setStatus(ExpenseOccurrenceStatus.PENDING);

        occurrenceRepository.save(occurrence);

        splitService.updateOccurrenceStatus(occurrence);
    }

    @Override
    public ExpenseOccurrence changePaidBy(ExpenseOccurrence occurrence, User newPaidBy, GroupMember member) {

        validator.validateCanChangePaidBy(occurrence, newPaidBy, member);

        occurrence.setPaidBy(newPaidBy);

        ExpenseOccurrence saved = occurrenceRepository.save(occurrence);

        splitService.recalculateParticipants(saved);

        return saved;
    }

    @Override
    public void updateFutureOccurrences(RecurringExpense recurringExpense) {

        List<ExpenseOccurrence> futureOccurrences = occurrenceRepository.findFutureOccurrences(recurringExpense, LocalDate.now());

        futureOccurrences.forEach(occurrence -> {

            occurrence.setName(recurringExpense.getName());
            occurrence.setAmount(recurringExpense.getAmount());
            occurrence.setPaidBy(recurringExpense.getPaidBy());

            splitService.recalculateParticipants(occurrence);
        });

        occurrenceRepository.saveAll(futureOccurrences);
    }

    @Override
    public void cancelFutureOccurrences(RecurringExpense recurringExpense, GroupMember member) {

        List<ExpenseOccurrence> futureOccurrences =
                occurrenceRepository.findFutureOccurrences(
                        recurringExpense,
                        LocalDate.now());

        futureOccurrences.forEach(occurrence -> {

            validator.validateCanCancelFuture(occurrence, member);

            occurrence.setStatus(
                    ExpenseOccurrenceStatus.CANCELLED);
        });

        occurrenceRepository.saveAll(futureOccurrences);
    }

    @Override
    public List<ExpenseOccurrence> generatePendingOccurrences(RecurringExpense recurringExpense, LocalDate until) {

        List<ExpenseOccurrence> generated = new ArrayList<>();

        LocalDate nextDueDate = occurrenceRepository
                .findTopByRecurringExpenseOrderByDueDateDesc(recurringExpense)
                .map(last -> nextOccurrenceDate(
                        last.getDueDate(),
                        recurringExpense.getFrequency()))
                .orElse(recurringExpense.getStartDate());

        while (!nextDueDate.isAfter(until)) {

            generated.add(
                    createOccurrence(
                            recurringExpense,
                            nextDueDate));

            nextDueDate = nextOccurrenceDate(
                    nextDueDate,
                    recurringExpense.getFrequency());
        }

        return generated;
    }

    private ExpenseOccurrence createOccurrence(RecurringExpense recurringExpense, LocalDate dueDate) {

        ExpenseOccurrence occurrence = ExpenseOccurrence.builder()
                .recurringExpense(recurringExpense)
                .group(recurringExpense.getGroup())
                .name(recurringExpense.getName())
                .amount(recurringExpense.getAmount())
                .paidBy(recurringExpense.getPaidBy())
                .dueDate(dueDate)
                .periodStart(dueDate)
                .periodEnd(calculatePeriodEnd(
                        dueDate,
                        recurringExpense.getFrequency()))
                .status(ExpenseOccurrenceStatus.PENDING)
                .build();

        validator.validateOccurrence(occurrence);

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