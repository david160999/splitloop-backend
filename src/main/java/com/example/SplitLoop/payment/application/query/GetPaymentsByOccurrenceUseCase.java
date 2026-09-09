package com.example.SplitLoop.payment.application.query;

import com.example.SplitLoop.common.util.PageResponse;
import com.example.SplitLoop.common.util.PageResponseMapper;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.expense.exception.ExpenseOccurrenceNotFoundException;
import com.example.SplitLoop.payment.controller.query.GetPaymentsByOccurrenceQuery;
import com.example.SplitLoop.payment.controller.response.PaymentResponse;
import com.example.SplitLoop.payment.domain.entity.Payment;
import com.example.SplitLoop.payment.domain.repository.PaymentRepository;
import com.example.SplitLoop.payment.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPaymentsByOccurrenceUseCase {

    private final ExpenseOccurrenceRepository occurrenceRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper mapper;

    @Transactional(readOnly = true)
    public PageResponse<PaymentResponse> execute(
            GetPaymentsByOccurrenceQuery request,
            Pageable pageable) {

        ExpenseOccurrence occurrence = occurrenceRepository.findById(request.getOccurrenceId())
                .orElseThrow(() ->
                        new ExpenseOccurrenceNotFoundException(request.getOccurrenceId()));

        Page<Payment> page = paymentRepository.findByOccurrence(occurrence, pageable);

        return PageResponseMapper.map(page, mapper::toResponse);
    }
}
