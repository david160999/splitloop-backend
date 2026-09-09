package com.example.SplitLoop.expense.domain.service;

import com.example.SplitLoop.expense.domain.entity.*;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceSplitRepository;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseParticipantRepository;
import com.example.SplitLoop.expense.domain.service.splitStrategy.SplitStrategy;
import com.example.SplitLoop.expense.domain.service.splitStrategy.SplitStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseOccurrenceSplitServiceImpl implements ExpenseOccurrenceSplitService {

    private final RecurringExpenseParticipantRepository participantRepository;

    private final ExpenseOccurrenceSplitRepository splitRepository;

    private final SplitStrategyFactory strategyFactory;

    @Override
    public void recalculateParticipants(ExpenseOccurrence occurrence) {

        splitRepository.deleteByOccurrence(occurrence);

        List<RecurringExpenseParticipant> participants = participantRepository.findByRecurringExpense(occurrence.getRecurringExpense());

        SplitStrategy strategy = strategyFactory.getStrategy(occurrence.getRecurringExpense().getSplitType());

        List<ExpenseOccurrenceSplit> splits = strategy.calculate(occurrence, participants);

        splitRepository.saveAll(splits);

        updateOccurrenceStatus(occurrence);
    }

    @Override
    public void updateOccurrenceStatus(ExpenseOccurrence occurrence) {

        List<ExpenseOccurrenceSplit> debtorSplits = splitRepository
                .findByOccurrence(occurrence)
                .stream()
                .filter(split -> !split.getUser().equals(occurrence.getPaidBy()))
                .toList();

        boolean allPaid = debtorSplits.stream()
                .allMatch(split ->
                        split.getStatus() == ExpenseOccurrenceSplitStatus.PAID);

        if (allPaid) {
            occurrence.setStatus(ExpenseOccurrenceStatus.PAID);
            return;
        }

        boolean partiallyPaid = debtorSplits.stream()
                .anyMatch(split ->
                        split.getStatus() == ExpenseOccurrenceSplitStatus.PAID
                                || split.getStatus() == ExpenseOccurrenceSplitStatus.PARTIALLY_PAID);

        occurrence.setStatus(
                partiallyPaid
                        ? ExpenseOccurrenceStatus.PARTIALLY_PAID
                        : ExpenseOccurrenceStatus.PENDING
        );

    }
}
