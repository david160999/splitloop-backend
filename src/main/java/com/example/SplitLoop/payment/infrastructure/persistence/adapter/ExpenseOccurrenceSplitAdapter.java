package com.example.SplitLoop.payment.infrastructure.persistence.adapter;

import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceSplitRepository;
import com.example.SplitLoop.payment.domain.port.ExpenseOccurrenceSplitPort;
import com.example.SplitLoop.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExpenseOccurrenceSplitAdapter implements ExpenseOccurrenceSplitPort {

    private final ExpenseOccurrenceSplitRepository splitRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public Optional<ExpenseOccurrenceSplit> findById(UUID splitId) {
        return splitRepository.findById(splitId);
    }

    @Override
    public void updateStatusAfterPayment(UUID splitId) {
        splitRepository.findById(splitId).ifPresent(split -> {
            // 1. Obtener la suma real acumulada de pagos para este split
            BigDecimal totalPaid = paymentRepository.sumAmountBySplitId(splitId);

            // 2. Invocar la función de dominio del record para obtener la nueva versión inmutable
            ExpenseOccurrenceSplit updatedSplit = split.calculateStatus(totalPaid);

            // 3. Persistir el split con su nuevo estado (PENDING, PARTIALLY_PAID o PAID)
            splitRepository.save(updatedSplit);
        });
    }
}