package com.example.SplitLoop.expense.application.query;

import com.example.SplitLoop.expense.controller.query.GetRecurringExpenseQuery;
import com.example.SplitLoop.expense.controller.response.RecurringExpenseResponse;
import com.example.SplitLoop.expense.domain.entity.RecurringExpense;
import com.example.SplitLoop.expense.domain.entity.RecurringExpenseParticipant;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseParticipantRepository;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.expense.exception.RecurringExpenseNotFoundException;
import com.example.SplitLoop.expense.mapper.RecurringExpenseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetRecurringExpenseUseCase {

    private final RecurringExpenseRepository recurringExpenseRepository;
    private final RecurringExpenseParticipantRepository participantRepository;

    private final RecurringExpenseMapper mapper;

    @Transactional(readOnly = true)
    public RecurringExpenseResponse execute(UUID recurringExpenseId) {

        RecurringExpense recurringExpense = recurringExpenseRepository.findById(recurringExpenseId)
                .orElseThrow(() -> new RecurringExpenseNotFoundException(recurringExpenseId));

        List<RecurringExpenseParticipant> participants = participantRepository.findByRecurringExpense(recurringExpense);

        RecurringExpenseResponse response = mapper.toResponse(recurringExpense);

        response.setParticipants(mapper.toParticipantResponses(participants));

        return response;
    }
}