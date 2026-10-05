package com.example.SplitLoop.payment.application.usecase.command;

import com.example.SplitLoop.group.domain.exception.GroupMemberNotFoundException;
import com.example.SplitLoop.payment.application.dto.mapper.PaymentDtoMapper;
import com.example.SplitLoop.payment.domain.model.Payment;
import com.example.SplitLoop.payment.domain.model.PaymentType;
import com.example.SplitLoop.payment.domain.policy.PaymentRefundPolicy;
import com.example.SplitLoop.payment.domain.port.GroupMemberPort;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.common.application.service.CurrentUserService;
import com.example.SplitLoop.payment.application.dto.request.RefundPaymentRequest;
import com.example.SplitLoop.payment.application.dto.response.PaymentResponse;
import com.example.SplitLoop.payment.domain.repository.PaymentRepository;
import com.example.SplitLoop.payment.domain.exception.PaymentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefundPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final GroupMemberPort memberPort;
    private final CurrentUserService currentUserService;
    private final PaymentDtoMapper mapper;
    private final List<PaymentRefundPolicy> refundPolicies; // Inyección implícita de todas las políticas

    @Transactional
    public PaymentResponse execute(UUID paymentId, RefundPaymentRequest request) {

        Payment originalPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        User currentUser = currentUserService.getCurrentUser();

        UUID groupId = originalPayment.occurrence().group().id();
        memberPort.findByGroupIdAndUserId(groupId, currentUser.id())
                .orElseThrow(() -> new GroupMemberNotFoundException(groupId, currentUser.id()));

        // Validar todas las políticas de reembolso registradas
        refundPolicies.forEach(policy -> policy.validateCanRefund(originalPayment, request.getAmount()));

        // Crear modelo inmutable
        Payment refundPayment = Payment.builder()
                .occurrence(originalPayment.occurrence())
                .split(originalPayment.split())
                .fromUser(originalPayment.fromUser())
                .toUser(originalPayment.toUser())
                .createdBy(currentUser)
                .amount(request.getAmount().negate())
                .type(PaymentType.REFUND)
                .note(originalPayment.note())
                .build();

        Payment savedRefund = paymentRepository.save(refundPayment);

        return mapper.toResponse(savedRefund);
    }
}
