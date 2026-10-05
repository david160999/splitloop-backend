package com.example.SplitLoop.expense.application.usecase.query;

import com.example.SplitLoop.expense.application.dto.mapper.ExpenseDtoMapper;
import com.example.SplitLoop.expense.application.dto.query.GetOccurrencesQuery;
import com.example.SplitLoop.expense.application.dto.response.ExpenseOccurrenceResponse;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceCriteria;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceSplitRepository;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.expense.domain.repository.RecurringExpenseRepository;
import com.example.SplitLoop.expense.domain.exception.RecurringExpenseNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetOccurrencesUseCase {

    private final RecurringExpenseRepository recurringExpenseRepository;
    private final ExpenseOccurrenceRepository occurrenceRepository;
    private final ExpenseOccurrenceSplitRepository splitRepository;
    private final ExpenseDtoMapper mapper;

    @Transactional(readOnly = true)
    public List<ExpenseOccurrenceResponse> execute(GetOccurrencesQuery request) {

        // 1. Validar la existencia de la expensa recurrente
        recurringExpenseRepository.findById(request.getRecurringExpenseId())
                .orElseThrow(() -> new RecurringExpenseNotFoundException(request.getRecurringExpenseId()));

        // 2. Construir criterio de dominio
        ExpenseOccurrenceCriteria criteria = ExpenseOccurrenceCriteria.builder()
                .recurringExpenseId(request.getRecurringExpenseId())
                .status(request.getStatus())
                .from(request.getFrom())
                .to(request.getTo())
                .build();

        // 3. Consultar ocurrencias en el puerto
        List<ExpenseOccurrence> occurrences = occurrenceRepository.findAll(criteria);

        if (occurrences.isEmpty()) {
            return Collections.emptyList();
        }

        // 4. Cargar splits de forma agrupada para evitar problema N+1
        List<UUID> occurrenceIds = occurrences.stream()
                .map(ExpenseOccurrence::id)
                .toList();

        Map<UUID, List<ExpenseOccurrenceSplit>> splitsByOccurrenceId =
                splitRepository.findByOccurrenceIdIn(occurrenceIds).stream()
                        .collect(Collectors.groupingBy(split -> split.occurrence().id()));

        // 5. Mapear a DTOs de respuesta usando la sobrecarga con splits
        return occurrences.stream()
                .map(occurrence -> mapper.toResponse(
                        occurrence,
                        splitsByOccurrenceId.getOrDefault(occurrence.id(), Collections.emptyList())
                ))
                .toList();
    }
}

