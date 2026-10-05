package com.example.SplitLoop.payment.application.usecase.query;

import com.example.SplitLoop.common.domain.model.PageResponse;
import com.example.SplitLoop.common.domain.mapper.PageResponseMapper;
import com.example.SplitLoop.group.domain.exception.UserNotFoundException;
import com.example.SplitLoop.payment.application.dto.mapper.PaymentDtoMapper;
import com.example.SplitLoop.payment.application.dto.response.PaymentResponse;
import com.example.SplitLoop.payment.domain.model.Payment;
import com.example.SplitLoop.payment.domain.repository.PaymentRepository;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetPaymentsByUserUseCase {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentDtoMapper mapper;

    @Transactional(readOnly = true)
    public PageResponse<PaymentResponse> execute(UUID userId, Pageable pageable) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        Page<Payment> page = paymentRepository.findByFromUserIdOrToUserId(user.id(), user.id(), pageable);

        return PageResponseMapper.map(page, mapper::toResponse);

    }
}