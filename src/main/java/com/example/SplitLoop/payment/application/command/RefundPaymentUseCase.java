package com.example.SplitLoop.payment.application.command;

import com.example.SplitLoop.user.domain.service.CurrentUserService;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.group.domain.repository.GroupMemberRepository;
import com.example.SplitLoop.group.exception.UserNotInGroupException;
import com.example.SplitLoop.payment.controller.command.RefundPaymentCommand;
import com.example.SplitLoop.payment.controller.response.PaymentResponse;
import com.example.SplitLoop.payment.domain.entity.Payment;
import com.example.SplitLoop.payment.domain.repository.PaymentRepository;
import com.example.SplitLoop.payment.domain.service.PaymentService;
import com.example.SplitLoop.payment.exception.PaymentNotFoundException;
import com.example.SplitLoop.payment.mapper.PaymentMapper;
import com.example.SplitLoop.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefundPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final GroupMemberRepository groupMemberRepository;

    private final CurrentUserService currentUserService;

    private final PaymentService paymentService;
    private final PaymentMapper mapper;

    @Transactional
    public PaymentResponse execute(UUID paymentId, RefundPaymentCommand request) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        User currentUser = currentUserService.getCurrentUser();

        GroupMember member = groupMemberRepository
                .findByGroupAndUser(payment.getOccurrence().getGroup(), currentUser)
                .orElseThrow(() -> new UserNotInGroupException(currentUser.getId(), payment.getOccurrence().getGroup().getId()));

        Payment refund = paymentService.refundPayment(
                payment,
                member,
                currentUser,
                request.getAmount());

        return mapper.toResponse(refund);
    }
}
