package com.example.SplitLoop.payment.application.query;

import com.example.SplitLoop.common.util.PageResponse;
import com.example.SplitLoop.common.util.PageResponseMapper;
import com.example.SplitLoop.group.exception.UserNotFoundException;
import com.example.SplitLoop.payment.controller.query.GetPaymentsByUserQuery;
import com.example.SplitLoop.payment.controller.response.PaymentResponse;
import com.example.SplitLoop.payment.domain.entity.Payment;
import com.example.SplitLoop.payment.domain.repository.PaymentRepository;
import com.example.SplitLoop.payment.mapper.PaymentMapper;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetPaymentsByUserUseCase {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper mapper;

    @Transactional(readOnly = true)
    public PageResponse<PaymentResponse> execute(GetPaymentsByUserQuery request, Pageable pageable) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));

        Page<Payment> page = paymentRepository.findByFromUserOrToUser(user, user, pageable);

        return PageResponseMapper.map(page, mapper::toResponse);

    }
}