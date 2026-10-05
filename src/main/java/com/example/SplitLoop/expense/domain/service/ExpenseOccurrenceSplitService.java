package com.example.SplitLoop.expense.domain.service;

import com.example.SplitLoop.expense.domain.model.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceSplitStatus;
import com.example.SplitLoop.expense.domain.model.RecurringExpenseParticipant;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceSplitRepository;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseParticipantRepository;
import com.example.SplitLoop.expense.domain.service.splitStrategy.SplitStrategy;
import com.example.SplitLoop.expense.domain.service.splitStrategy.SplitStrategyFactory;

import java.util.List;

public class ExpenseOccurrenceSplitService {
    private final RecurringExpenseParticipantRepository participantRepository;
    private final ExpenseOccurrenceSplitRepository splitRepository;
    private final SplitStrategyFactory strategyFactory;

    public ExpenseOccurrenceSplitService(RecurringExpenseParticipantRepository participantRepository, ExpenseOccurrenceSplitRepository splitRepository, SplitStrategyFactory strategyFactory) {
        this.participantRepository = participantRepository;
        this.splitRepository = splitRepository;
        this.strategyFactory = strategyFactory;
    }

    public void recalculateParticipants(ExpenseOccurrence occurrence) {

        splitRepository.findByOccurrenceId(occurrence.id());

        List<RecurringExpenseParticipant> participants = participantRepository.findByRecurringExpenseId(occurrence.recurringExpense().id());

        SplitStrategy strategy = strategyFactory.getStrategy(occurrence.recurringExpense().splitType());

        List<ExpenseOccurrenceSplit> splits = strategy.calculate(occurrence, participants);

        splitRepository.saveAll(splits);

        updateOccurrenceStatus(occurrence);
    }

    public void updateOccurrenceStatus(ExpenseOccurrence occurrence) {

        List<ExpenseOccurrenceSplit> debtorSplits = splitRepository
                .findByOccurrenceId(occurrence.id())
                .stream()
                .filter(split -> !split.user().equals(occurrence.paidBy()))
                .toList();

        boolean allPaid = debtorSplits.stream()
                .allMatch(split ->
                        split.status() == ExpenseOccurrenceSplitStatus.PAID);

        if (allPaid) {
            occurrence.markAsPaid();
            return;
        }

        boolean partiallyPaid = debtorSplits.stream()
                .anyMatch(split ->
                        split.status() == ExpenseOccurrenceSplitStatus.PAID
                                || split.status() == ExpenseOccurrenceSplitStatus.PARTIALLY_PAID);

        if (partiallyPaid) {
            occurrence.markAsPartiallyPaid();
        } else {
            occurrence.markAsPending();
        }
    }
}
