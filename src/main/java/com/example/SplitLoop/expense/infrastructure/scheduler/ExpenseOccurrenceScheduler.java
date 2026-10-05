package com.example.SplitLoop.expense.infrastructure.scheduler;

import com.example.SplitLoop.expense.domain.model.RecurringExpense;
import com.example.SplitLoop.expense.domain.model.RecurringExpenseStatus;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.expense.domain.service.ExpenseOccurrenceService;
import com.example.SplitLoop.expense.domain.service.RecurringExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpenseOccurrenceScheduler {

    private final RecurringExpenseRepository recurringExpenseRepository;
    private final ExpenseOccurrenceService occurrenceService;
    private final RecurringExpenseService recurringExpenseService;

    @Scheduled(cron = "${application.scheduler.generate-occurrences}")
    public void generateOccurrences() {
        LocalDate today = LocalDate.now();

        List<RecurringExpense> recurringExpense = recurringExpenseRepository.findByStatusAndStartDateLessThanEqual(RecurringExpenseStatus.ACTIVE, today);

        recurringExpense.forEach(expense -> {
            recurringExpenseService.completeIfExpired(expense, today);

            if (expense.status() == RecurringExpenseStatus.ACTIVE) {
                occurrenceService.generatePendingOccurrences(expense, today);
            }
        });    }
}