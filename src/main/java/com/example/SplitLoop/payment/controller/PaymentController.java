package com.example.SplitLoop.payment.controller;

import com.example.SplitLoop.common.util.PageResponse;
import com.example.SplitLoop.payment.application.command.RefundPaymentUseCase;
import com.example.SplitLoop.payment.application.command.RegisterPaymentUseCase;
import com.example.SplitLoop.payment.application.query.GetPaymentUseCase;
import com.example.SplitLoop.payment.application.query.GetPaymentsByOccurrenceUseCase;
import com.example.SplitLoop.payment.application.query.GetPaymentsByUserUseCase;
import com.example.SplitLoop.payment.application.query.GetPaymentsUseCase;
import com.example.SplitLoop.payment.controller.command.RefundPaymentCommand;
import com.example.SplitLoop.payment.controller.command.RegisterPaymentCommand;
import com.example.SplitLoop.payment.controller.query.GetPaymentsByOccurrenceQuery;
import com.example.SplitLoop.payment.controller.query.GetPaymentsByUserQuery;
import com.example.SplitLoop.payment.controller.query.GetPaymentsQuery;
import com.example.SplitLoop.payment.controller.response.PaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<PaymentResponse> registerPayment(@Valid @RequestBody RegisterPaymentCommand request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(registerPaymentUseCase.execute(request));
    }

    @PostMapping("/{paymentId}/refund")
    @Operation(summary = "Refund payment")
    public ResponseEntity<PaymentResponse> refundPayment(
            @PathVariable UUID paymentId,
            @Valid @RequestBody RefundPaymentCommand request) {

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
            @Valid GetPaymentsByOccurrenceQuery query,
            Pageable pageable) {

        return ResponseEntity.ok(getPaymentsByOccurrenceUseCase.execute(query, pageable));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user payments")
    public ResponseEntity<PageResponse<PaymentResponse>> getPaymentsByUser(
            @Valid GetPaymentsByUserQuery query,
            Pageable pageable) {

        return ResponseEntity.ok(getPaymentsByUserUseCase.execute(query, pageable));
    }

}
