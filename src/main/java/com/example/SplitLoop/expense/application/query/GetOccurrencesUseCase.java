package com.example.SplitLoop.expense.application.query;

import com.example.SplitLoop.expense.controller.query.GetOccurrencesQuery;
import com.example.SplitLoop.expense.controller.response.ExpenseOccurrenceResponse;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.entity.RecurringExpense;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.expense.exception.RecurringExpenseNotFoundException;
import com.example.SplitLoop.expense.infrastructure.specification.ExpenseOccurrenceSpecifications;
import com.example.SplitLoop.expense.mapper.ExpenseOccurrenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetOccurrencesUseCase {

    private final RecurringExpenseRepository recurringExpenseRepository;
    private final ExpenseOccurrenceRepository occurrenceRepository;
    private final ExpenseOccurrenceMapper mapper;

    @Transactional(readOnly = true)
    public List<ExpenseOccurrenceResponse> execute(GetOccurrencesQuery request) {

        recurringExpenseRepository.findById(request.getRecurringExpenseId())
                .orElseThrow(() -> new RecurringExpenseNotFoundException(request.getRecurringExpenseId()));

        Specification<ExpenseOccurrence> spec = Specification.where(
                ExpenseOccurrenceSpecifications.hasRecurringExpense(
                        request.getRecurringExpenseId()));

        if (request.getStatus() != null) {
            spec = spec.and(ExpenseOccurrenceSpecifications.hasStatus(request.getStatus()));
        }

        if (request.getFrom() != null) {
            spec = spec.and(ExpenseOccurrenceSpecifications.dueDateAfter(request.getFrom()));
        }

        if (request.getTo() != null) {
            spec = spec.and(ExpenseOccurrenceSpecifications.dueDateBefore(request.getTo()));
        }

        return occurrenceRepository.findAll(spec)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}

