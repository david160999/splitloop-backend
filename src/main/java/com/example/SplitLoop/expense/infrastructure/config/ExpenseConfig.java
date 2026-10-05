package com.example.SplitLoop.expense.infrastructure.config;

import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceSplitRepository;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseParticipantRepository;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.expense.domain.service.ExpenseOccurrenceService;
import com.example.SplitLoop.expense.domain.service.ExpenseOccurrenceSplitService;
import com.example.SplitLoop.expense.domain.service.RecurringExpenseService;
import com.example.SplitLoop.expense.domain.service.splitStrategy.SplitStrategyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpenseConfig {

    // 1. Servicio de cálculo y reparto de Splits
    @Bean
    public ExpenseOccurrenceSplitService expenseOccurrenceSplitService(
            RecurringExpenseParticipantRepository participantRepository,
            ExpenseOccurrenceSplitRepository splitRepository,
            SplitStrategyFactory strategyFactory
    ) {
        return new ExpenseOccurrenceSplitService(
                participantRepository,
                splitRepository,
                strategyFactory
        );
    }

    // 2. Servicio de gestión de Ocurrencias (inyecta el SplitService configurado arriba)
    @Bean
    public ExpenseOccurrenceService expenseOccurrenceService(
            ExpenseOccurrenceRepository occurrenceRepository,
            ExpenseOccurrenceSplitService splitService
    ) {
        return new ExpenseOccurrenceService(
                occurrenceRepository,
                splitService
        );
    }

    // 3. Servicio principal de Gastos Recurrentes (inyecta el OccurrenceService configurado arriba)
    @Bean
    public RecurringExpenseService recurringExpenseService(
            RecurringExpenseRepository recurringExpenseRepository,
            RecurringExpenseParticipantRepository participantRepository,
            ExpenseOccurrenceService occurrenceService
    ) {
        return new RecurringExpenseService(
                recurringExpenseRepository,
                participantRepository,
                occurrenceService
        );
    }
}
