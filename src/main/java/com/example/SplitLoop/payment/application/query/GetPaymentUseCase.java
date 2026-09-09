package com.example.SplitLoop.payment.application.query;

import com.example.SplitLoop.payment.controller.response.PaymentResponse;
import com.example.SplitLoop.payment.domain.entity.Payment;
import com.example.SplitLoop.payment.domain.repository.PaymentRepository;
import com.example.SplitLoop.payment.exception.PaymentNotFoundException;
import com.example.SplitLoop.payment.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper mapper;

    @Transactional(readOnly = true)
    public PaymentResponse execute(UUID paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        return mapper.toResponse(payment);
    }
}