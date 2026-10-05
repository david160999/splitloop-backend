package com.example.SplitLoop.expense.application.usecase.query;

import com.example.SplitLoop.expense.application.dto.mapper.ExpenseDtoMapper;
import com.example.SplitLoop.expense.application.dto.response.ExpenseOccurrenceResponse;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceSplitRepository;
import com.example.SplitLoop.expense.domain.exception.ExpenseOccurrenceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetOccurrenceUseCase {

    private final ExpenseOccurrenceRepository occurrenceRepository;
    private final ExpenseOccurrenceSplitRepository splitRepository;
    private final ExpenseDtoMapper mapper;

    @Transactional(readOnly = true)
    public ExpenseOccurrenceResponse execute(UUID occurrenceId) {

        ExpenseOccurrence occurrence = occurrenceRepository.findById(occurrenceId)
                .orElseThrow(() -> new ExpenseOccurrenceNotFoundException(occurrenceId));

        // 2. Obtener los splits de dominio
        List<ExpenseOccurrenceSplit> splits = splitRepository.findByOccurrenceId(occurrenceId);

        // 3. Mapear todo en una sola llamada (crea la instancia inmutable del record DTO)
        return mapper.toResponse(occurrence, splits);

    }
}
