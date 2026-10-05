package com.example.SplitLoop.payment.infrastructure.presentation.rest.controller;

import com.example.SplitLoop.common.domain.model.PageResponse;
import com.example.SplitLoop.payment.application.usecase.command.RefundPaymentUseCase;
import com.example.SplitLoop.payment.application.usecase.command.RegisterPaymentUseCase;
import com.example.SplitLoop.payment.application.usecase.query.GetPaymentUseCase;
import com.example.SplitLoop.payment.application.usecase.query.GetPaymentsByOccurrenceUseCase;
import com.example.SplitLoop.payment.application.usecase.query.GetPaymentsByUserUseCase;
import com.example.SplitLoop.payment.application.usecase.query.GetPaymentsUseCase;
import com.example.SplitLoop.payment.application.dto.request.RefundPaymentRequest;
import com.example.SplitLoop.payment.application.dto.request.RegisterPaymentRequest;
import com.example.SplitLoop.payment.application.dto.query.GetPaymentsByOccurrenceQuery;
import com.example.SplitLoop.payment.application.dto.query.GetPaymentsByUserQuery;
import com.example.SplitLoop.payment.application.dto.query.GetPaymentsQuery;
import com.example.SplitLoop.payment.application.dto.response.PaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payments")
public class PaymentController {

    private final RegisterPaymentUseCase registerPaymentUseCase;
    private final RefundPaymentUseCase refundPaymentUseCase;

    private final GetPaymentUseCase getPaymentUseCase;
    private final GetPaymentsUseCase getPaymentsUseCase;
    private final GetPaymentsByOccurrenceUseCase getPaymentsByOccurrenceUseCase;
    private final GetPaymentsByUserUseCase getPaymentsByUserUseCase;

    @PostMapping
    @Operation(summary = "Register payment")
    public ResponseEntity<PaymentResponse> registerPayment(@Valid @RequestBody RegisterPaymentRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(registerPaymentUseCase.execute(request));
    }

    @PostMapping("/{paymentId}/refund")
    @Operation(summary = "Refund payment")
    public ResponseEntity<PaymentResponse> refundPayment(
            @PathVariable UUID paymentId,
            @Valid @RequestBody RefundPaymentRequest request) {

        return ResponseEntity.ok(refundPaymentUseCase.execute(paymentId, request));
    }

    @GetMapping("/{paymentId}")
    @Operation(summary = "Get payment")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable UUID paymentId) {

        return ResponseEntity.ok(getPaymentUseCase.execute(paymentId));
    }

    @GetMapping
    @Operation(summary = "Get payments")
    public ResponseEntity<PageResponse<PaymentResponse>> getPayments(
            @Valid GetPaymentsQuery query,
            Pageable pageable) {

        return ResponseEntity.ok(getPaymentsUseCase.execute(query, pageable));
    }

    @GetMapping("/occurrence/{occurrenceId}")
    @Operation(summary = "Get occurrence payments")
    public ResponseEntity<PageResponse<PaymentResponse>> getPaymentsByOccurrence(
            @PathVariable UUID occurrenceId,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {

        PageResponse<PaymentResponse> response =  getPaymentsByOccurrenceUseCase.execute(occurrenceId, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user payments")
    public ResponseEntity<PageResponse<PaymentResponse>> getPaymentsByUser(
            @PathVariable UUID userId,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {

        PageResponse<PaymentResponse> response = getPaymentsByUserUseCase.execute(userId, pageable);

        return ResponseEntity.ok(response);
    }

}
