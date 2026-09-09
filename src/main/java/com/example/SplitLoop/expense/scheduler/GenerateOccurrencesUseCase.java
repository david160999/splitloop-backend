package com.example.SplitLoop.expense.scheduler;

import com.example.SplitLoop.expense.domain.entity.RecurringExpense;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseStatus;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.expense.domain.service.ExpenseOccurrenceService;
import com.example.SplitLoop.expense.domain.service.RecurringExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenerateOccurrencesUseCase {

    private final RecurringExpenseRepository recurringExpenseRepository;
    private final ExpenseOccurrenceService occurrenceService;
    private final RecurringExpenseService recurringExpenseService;

    @Transactional
    public void execute() {

        LocalDate today = LocalDate.now();

        List<RecurringExpense> recurringExpenses = recurringExpenseRepository.findByStatusAndStartDateLessThanEqual(RecurringExpenseStatus.ACTIVE, today);

        recurringExpenses.forEach(expense -> {
            recurringExpenseService.completeIfExpired(expense, today);

            if (expense.getStatus() == RecurringExpenseStatus.ACTIVE) {
                occurrenceService.generatePendingOccurrences(expense, today);
            }
        });
    }
}
