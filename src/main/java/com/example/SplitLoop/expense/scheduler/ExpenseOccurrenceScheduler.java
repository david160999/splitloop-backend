package com.example.SplitLoop.expense.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpenseOccurrenceScheduler {

    private final GenerateOccurrencesUseCase generateOccurrencesUseCase;

    @Scheduled(cron = "${application.scheduler.generate-occurrences}")
    public void generateOccurrences() {
        generateOccurrencesUseCase.execute();
    }
}