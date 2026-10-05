package com.example.SplitLoop.payment.application.usecase.query;

import com.example.SplitLoop.common.domain.model.PageResponse;
import com.example.SplitLoop.common.domain.mapper.PageResponseMapper;
import com.example.SplitLoop.payment.application.dto.mapper.PaymentDtoMapper;
import com.example.SplitLoop.payment.application.dto.query.GetPaymentsQuery;
import com.example.SplitLoop.payment.application.dto.response.PaymentResponse;
import com.example.SplitLoop.payment.domain.model.Payment;
import com.example.SplitLoop.payment.domain.model.PaymentCriteria;
import com.example.SplitLoop.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class GetPaymentsUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentDtoMapper mapper;

    @Transactional(readOnly = true)
    public PageResponse<PaymentResponse> execute(GetPaymentsQuery request, Pageable pageable) {

        // 1. Mapear el Request HTTP/DTO a un objeto de criterio de dominio
        PaymentCriteria criteria = PaymentCriteria.builder()
                .groupId(request.groupId())
                .occurrenceId(request.occurrenceId())
                .userId(request.userId())
                .type(request.type())
                .from(request.from())
                .to(request.to())
                .build();

        // 2. Consultar al puerto de persistencia
        Page<Payment> domainPage = paymentRepository.findAll(criteria, pageable);

        // 3. Mapear la página de dominio al DTO de respuesta paginado
        return PageResponseMapper.map(domainPage, mapper::toResponse);
    }
}