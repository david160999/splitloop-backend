package com.example.SplitLoop.payment.application.query;

import com.example.SplitLoop.common.util.PageResponse;
import com.example.SplitLoop.payment.controller.query.GetPaymentsQuery;
import com.example.SplitLoop.payment.controller.response.PaymentResponse;
import com.example.SplitLoop.payment.domain.entity.Payment;
import com.example.SplitLoop.payment.domain.repository.PaymentRepository;
import com.example.SplitLoop.payment.infrastructure.specification.PaymentSpecification;
import com.example.SplitLoop.payment.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPaymentsUseCase {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper mapper;

    @Transactional(readOnly = true)
    public PageResponse<PaymentResponse> execute(GetPaymentsQuery request, Pageable pageable) {

        Specification<Payment> spec = Specification.where((Specification<Payment>) null);

        if (request.getGroupId() != null) {
            spec = spec.and(PaymentSpecification.hasGroup(request.getGroupId()));
        }

        if (request.getOccurrenceId() != null) {
            spec = spec.and(PaymentSpecification.hasOccurrence(request.getOccurrenceId()));
        }

        if (request.getUserId() != null) {
            spec = spec.and(PaymentSpecification.hasUser(request.getUserId()));
        }

        if (request.getType() != null) {
            spec = spec.and(PaymentSpecification.hasType(request.getType()));
        }

        if (request.getFrom() != null) {
            spec = spec.and(PaymentSpecification.paidAfter(request.getFrom()));
        }

        if (request.getTo() != null) {
            spec = spec.and(PaymentSpecification.paidBefore(request.getTo()));
        }

        Page<Payment> page = paymentRepository.findAll(spec, pageable);

        return PageResponse.<PaymentResponse>builder()
                .content(page.map(mapper::toResponse).getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}