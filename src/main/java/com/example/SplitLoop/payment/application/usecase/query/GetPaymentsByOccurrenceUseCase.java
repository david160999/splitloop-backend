package com.example.SplitLoop.payment.application.usecase.query;

import com.example.SplitLoop.common.domain.model.PageResponse;
import com.example.SplitLoop.common.domain.mapper.PageResponseMapper;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.expense.domain.exception.ExpenseOccurrenceNotFoundException;
import com.example.SplitLoop.payment.application.dto.mapper.PaymentDtoMapper;
import com.example.SplitLoop.payment.application.dto.response.PaymentResponse;
import com.example.SplitLoop.payment.domain.model.Payment;
import com.example.SplitLoop.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetPaymentsByOccurrenceUseCase {

    private final ExpenseOccurrenceRepository occurrenceRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentDtoMapper mapper;

    @Transactional(readOnly = true)
    public PageResponse<PaymentResponse> execute(UUID occurrenceId, Pageable pageable) {

        ExpenseOccurrence occurrence = occurrenceRepository.findById(occurrenceId)
                .orElseThrow(() -> new ExpenseOccurrenceNotFoundException(occurrenceId));

        Page<Payment> page = paymentRepository.findByOccurrenceId(occurrence.id(), pageable);

        return PageResponseMapper.map(page, mapper::toResponse);
    }
}
