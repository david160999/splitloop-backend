package com.example.SplitLoop.payment.infrastructure.persistence.adapter;

import com.example.SplitLoop.expense.domain.model.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceSplitRepository;
import com.example.SplitLoop.expense.infrastructure.persistence.jpa.SpringDataExpenseOccurrenceRepository;
import com.example.SplitLoop.payment.domain.port.ExpenseOccurrencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExpenseOccurrenceAdapter implements ExpenseOccurrencePort {

    private final ExpenseOccurrenceRepository occurrenceRepository;
    private final ExpenseOccurrenceSplitRepository splitRepository;

    @Override
    public Optional<ExpenseOccurrence> findById(UUID occurrenceId) {
        return occurrenceRepository.findById(occurrenceId);
    }

    @Override
    public void updateStatusAfterPayment(UUID occurrenceId) {
        occurrenceRepository.findById(occurrenceId).ifPresent(occurrence -> {
            // 1. Cargar desde BD la lista de splits actualizada para esta ocurrencia
            List<ExpenseOccurrenceSplit> splits = splitRepository.findByOccurrenceId(occurrenceId);

            // 2. Invocar la función pura de dominio pasando la lista de splits
            ExpenseOccurrence updatedOccurrence = occurrence.recalculateStatus(splits);

            // 3. Guardar el modelo inmutable actualizado
            occurrenceRepository.save(updatedOccurrence);
        });
    }
}