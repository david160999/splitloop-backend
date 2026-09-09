package com.example.SplitLoop.expense.application.query;

import com.example.SplitLoop.expense.controller.response.ExpenseOccurrenceResponse;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceSplitRepository;
import com.example.SplitLoop.expense.exception.ExpenseOccurrenceNotFoundException;
import com.example.SplitLoop.expense.mapper.ExpenseOccurrenceMapper;
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
    private final ExpenseOccurrenceMapper mapper;

    @Transactional(readOnly = true)
    public ExpenseOccurrenceResponse execute(UUID occurrenceId) {

        ExpenseOccurrence occurrence = occurrenceRepository.findById(occurrenceId)
                .orElseThrow(() -> new ExpenseOccurrenceNotFoundException(occurrenceId));

        List<ExpenseOccurrenceSplit> splits = splitRepository.findByOccurrence(occurrence);

        ExpenseOccurrenceResponse response = mapper.toResponse(occurrence);

        response.setSplits(mapper.toSplitResponses(splits));

        return response;    }
}
