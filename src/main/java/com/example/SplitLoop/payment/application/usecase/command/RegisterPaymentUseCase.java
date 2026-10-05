package com.example.SplitLoop.payment.application.usecase.command;

import com.example.SplitLoop.expense.domain.model.ExpenseOccurrence;
import com.example.SplitLoop.expense.domain.model.ExpenseOccurrenceSplit;
import com.example.SplitLoop.group.domain.exception.GroupMemberNotFoundException;
import com.example.SplitLoop.group.domain.model.GroupMember;
import com.example.SplitLoop.payment.application.dto.mapper.PaymentDtoMapper;
import com.example.SplitLoop.payment.domain.model.Payment;
import com.example.SplitLoop.payment.domain.model.PaymentType;
import com.example.SplitLoop.payment.domain.policy.PaymentRegistrationPolicy;
import com.example.SplitLoop.payment.domain.port.ExpenseOccurrencePort;
import com.example.SplitLoop.payment.domain.port.ExpenseOccurrenceSplitPort;
import com.example.SplitLoop.payment.domain.port.GroupMemberPort;
import com.example.SplitLoop.user.domain.model.User;
import com.example.SplitLoop.common.application.service.CurrentUserService;
import com.example.SplitLoop.expense.domain.exception.ExpenseOccurrenceNotFoundException;
import com.example.SplitLoop.expense.domain.exception.ExpenseOccurrenceSplitNotFoundException;
import com.example.SplitLoop.payment.application.dto.request.RegisterPaymentRequest;
import com.example.SplitLoop.payment.application.dto.response.PaymentResponse;
import com.example.SplitLoop.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final CurrentUserService currentUserService;
    private final PaymentDtoMapper mapper;

    // Inyección de políticas de pago y puertos de actualización
    private final List<PaymentRegistrationPolicy> registrationPolicies;
    private final GroupMemberPort memberPort;
    private final ExpenseOccurrencePort occurrencePort;
    private final ExpenseOccurrenceSplitPort splitPort;

    @Transactional
    public PaymentResponse execute(RegisterPaymentRequest request) {

        // 1. Obtener la ocurrencia y el split correspondiente (Modelos de Dominio)
        ExpenseOccurrence occurrence = occurrencePort.findById(request.getOccurrenceId())
                .orElseThrow(() -> new ExpenseOccurrenceNotFoundException(request.getOccurrenceId()));

        ExpenseOccurrenceSplit split = splitPort.findById(request.getSplitId())
                .orElseThrow(() -> new ExpenseOccurrenceSplitNotFoundException(request.getSplitId()));

        User createdBy = currentUserService.getCurrentUser();

        // 2. Verificar la membresía del usuario en el grupo mediante el puerto modular
        UUID groupId = occurrence.group().id();
        GroupMember member = memberPort.findByGroupIdAndUserId(groupId, createdBy.id())
                .orElseThrow(() -> new GroupMemberNotFoundException(groupId, createdBy.id()));

        // 3. Mapear y construir el modelo de dominio Payment
        Payment payment = Payment.builder()
                .occurrence(occurrence)
                .split(split)
                .fromUser(split.user())       // Quien debe el gasto
                .toUser(occurrence.paidBy())  // Quien pagó la ocurrencia
                .amount(request.getAmount())
                .note(request.getNote())
                .createdBy(createdBy)
                .type(PaymentType.PAYMENT)
                .build();

        // 4. Validar las políticas de registro de pago
        registrationPolicies.forEach(policy -> policy.validateCanRegister(payment, member));

        // 5. Persistir el pago
        Payment savedPayment = paymentRepository.save(payment);

        // 6. Recalcular y actualizar los estados del Split y la Ocurrencia
        splitPort.updateStatusAfterPayment(split.id());
        occurrencePort.updateStatusAfterPayment(occurrence.id());

        // 7. Retornar DTO de respuesta
        return mapper.toResponse(savedPayment);
    }
}
