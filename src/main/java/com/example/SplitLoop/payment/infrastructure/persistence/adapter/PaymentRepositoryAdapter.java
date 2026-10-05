package com.example.SplitLoop.payment.infrastructure.persistence.adapter;

import com.example.SplitLoop.payment.application.dto.query.GetPaymentsQuery;
import com.example.SplitLoop.payment.domain.model.Payment;
import com.example.SplitLoop.payment.domain.model.PaymentCriteria;
import com.example.SplitLoop.payment.infrastructure.persistence.entity.PaymentEntity;
import com.example.SplitLoop.payment.infrastructure.persistence.jpa.SpringDataPaymentRepository;
import com.example.SplitLoop.payment.infrastructure.persistence.mapper.PaymentPersistenceMapper;
import com.example.SplitLoop.payment.infrastructure.presentation.rest.specification.PaymentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import com.example.SplitLoop.payment.domain.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final SpringDataPaymentRepository jpaRepository;
    private final PaymentPersistenceMapper paymentMapper;

    @Override
    public Payment save(Payment payment) {
        PaymentEntity entity = paymentMapper.toEntity(payment);
        PaymentEntity savedEntity = jpaRepository.save(entity);
        return paymentMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(paymentMapper::toDomain);
    }

    @Override
    public Page<Payment> findByOccurrenceId(UUID occurrenceId, Pageable pageable) {
        return jpaRepository.findByOccurrenceId(occurrenceId, pageable)
                .map(paymentMapper::toDomain);
    }

    @Override
    public List<Payment> findBySplitId(UUID splitId) {
        return jpaRepository.findBySplitId(splitId)
                .stream()
                .map(paymentMapper::toDomain)
                .toList();
    }

    @Override
    public List<Payment> findByFromUserId(UUID fromUserId) {
        return jpaRepository.findByFromUserId(fromUserId)
                .stream()
                .map(paymentMapper::toDomain)
                .toList();
    }

    @Override
    public List<Payment> findByToUserId(UUID toUserId) {
        return jpaRepository.findByToUserId(toUserId)
                .stream()
                .map(paymentMapper::toDomain)
                .toList();
    }

    @Override
    public Page<Payment> findByFromUserIdOrToUserId(UUID fromUserId, UUID toUserId, Pageable pageable) {
        return jpaRepository.findByFromUserIdOrToUserId(fromUserId, toUserId, pageable)
                .map(paymentMapper::toDomain);

    }

    @Override
    public BigDecimal sumAmountBySplitId(UUID splitId) {
        return jpaRepository.sumAmountBySplitId(splitId);
    }

    @Override
    public Page<Payment> findAll(PaymentCriteria criteria, Pageable pageable) {

        // Construcción de la Specification JPA a partir del objeto de criterio
        Specification<PaymentEntity> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (criteria.groupId() != null) {
            spec = spec.and(PaymentSpecification.hasGroup(criteria.groupId()));
        }
        if (criteria.occurrenceId() != null) {
            spec = spec.and(PaymentSpecification.hasOccurrence(criteria.occurrenceId()));
        }
        if (criteria.userId() != null) {
            spec = spec.and(PaymentSpecification.hasUser(criteria.userId()));
        }
        if (criteria.type() != null) {
            spec = spec.and(PaymentSpecification.hasType(criteria.type()));
        }
        if (criteria.from() != null) {
            spec = spec.and(PaymentSpecification.paidAfter(criteria.from()));
        }
        if (criteria.to() != null) {
            spec = spec.and(PaymentSpecification.paidBefore(criteria.to()));
        }

        // Ejecutar consulta paginada y mapear Entidades a Modelos de Dominio
        Page<PaymentEntity> entityPage = jpaRepository.findAll(spec, pageable);

        return entityPage.map(paymentMapper::toDomain);
    }

}
