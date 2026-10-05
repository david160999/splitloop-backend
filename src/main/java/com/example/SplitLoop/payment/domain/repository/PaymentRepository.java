package com.example.SplitLoop.payment.domain.repository;

import com.example.SplitLoop.payment.domain.model.Payment;
import com.example.SplitLoop.payment.domain.model.PaymentCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(UUID id);

    Page<Payment> findByOccurrenceId(UUID occurrenceId, Pageable pageable);

    List<Payment> findBySplitId(UUID splitId);

    List<Payment> findByFromUserId(UUID fromUserId);

    List<Payment> findByToUserId(UUID toUserId);

    Page<Payment> findByFromUserIdOrToUserId(UUID fromUserId, UUID toUserId, Pageable pageable);

    BigDecimal sumAmountBySplitId(UUID splitId);

    Page<Payment> findAll(PaymentCriteria criteria, Pageable pageable);
}
