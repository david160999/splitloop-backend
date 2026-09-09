package com.example.SplitLoop.payment.application.command;

import com.example.SplitLoop.user.domain.service.CurrentUserService;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.entity.ExpenseOccurrenceSplit;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceRepository;
import com.example.SplitLoop.expense.domain.repository.ExpenseOccurrenceSplitRepository;
import com.example.SplitLoop.expense.exception.ExpenseOccurrenceNotFoundException;
import com.example.SplitLoop.expense.exception.ExpenseOccurrenceSplitNotFoundException;
import com.example.SplitLoop.group.domain.entity.GroupMember;
import com.example.SplitLoop.group.domain.repository.GroupMemberRepository;
import com.example.SplitLoop.group.exception.UserNotInGroupException;
import com.example.SplitLoop.payment.controller.command.RegisterPaymentCommand;
import com.example.SplitLoop.payment.controller.response.PaymentResponse;
import com.example.SplitLoop.payment.domain.entity.Payment;
import com.example.SplitLoop.payment.domain.repository.PaymentRepository;
import com.example.SplitLoop.payment.domain.service.PaymentService;
import com.example.SplitLoop.payment.mapper.PaymentMapper;
import com.example.SplitLoop.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final ExpenseOccurrenceRepository occurrenceRepository;
    private final ExpenseOccurrenceSplitRepository splitRepository;
    private final GroupMemberRepository groupMemberRepository;

    private final CurrentUserService currentUserService;

    private final PaymentMapper mapper;
    private final PaymentService paymentService;

    @Transactional
    public PaymentResponse execute(RegisterPaymentCommand request) {

        ExpenseOccurrence occurrence = occurrenceRepository
                .findById(request.getOccurrenceId())
                .orElseThrow(() -> new ExpenseOccurrenceNotFoundException(request.getOccurrenceId()));

        ExpenseOccurrenceSplit split = splitRepository
                .findById(request.getSplitId())
                .orElseThrow(() -> new ExpenseOccurrenceSplitNotFoundException(request.getSplitId()));

        User createdBy = currentUserService.getCurrentUser();

        GroupMember member = groupMemberRepository
                .findByGroupAndUser(occurrence.getGroup(), createdBy)
                .orElseThrow(() -> new UserNotInGroupException(createdBy.getId(), occurrence.getGroup().getId()));

        Payment payment = mapper.toEntity(
                request,
                occurrence,
                split,
                split.getUser(),
                occurrence.getPaidBy(),
                createdBy);

        Payment saved = paymentService.registerPayment(payment, member);

        return mapper.toResponse(saved);
    }
}
