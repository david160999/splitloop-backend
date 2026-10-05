package com.example.SplitLoop.payment.application.usecase.query;

import com.example.SplitLoop.payment.application.dto.mapper.PaymentDtoMapper;
import com.example.SplitLoop.payment.application.dto.response.PaymentResponse;
import com.example.SplitLoop.payment.domain.model.Payment;
import com.example.SplitLoop.payment.infrastructure.persistence.entity.PaymentEntity;
import com.example.SplitLoop.payment.domain.repository.PaymentRepository;
import com.example.SplitLoop.payment.domain.exception.PaymentNotFoundException;
import com.example.SplitLoop.payment.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentDtoMapper mapper;

    @Transactional(readOnly = true)
    public PaymentResponse execute(UUID paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        return mapper.toResponse(payment);
    }
}