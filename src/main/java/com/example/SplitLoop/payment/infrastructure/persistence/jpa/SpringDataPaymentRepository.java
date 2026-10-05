package com.example.SplitLoop.payment.infrastructure.persistence.jpa;

import com.example.SplitLoop.payment.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface SpringDataPaymentRepository extends JpaRepository<PaymentEntity, UUID>, JpaSpecificationExecutor<PaymentEntity> {

    Page<PaymentEntity> findByOccurrenceId(UUID occurrenceId, Pageable pageable);

    List<PaymentEntity> findBySplitId(UUID splitId);

    List<PaymentEntity> findByFromUserId(UUID fromUserId);

    List<PaymentEntity> findByToUserId(UUID toUserId);

    Page<PaymentEntity> findByFromUserIdOrToUserId(UUID fromUserId, UUID toUserId, Pageable pageable);

    @Query("""
        SELECT COALESCE(SUM(p.amount), 0)
        FROM PaymentEntity p
        WHERE p.split.id = :splitId
    """)
    BigDecimal sumAmountBySplitId(@Param("splitId") UUID splitId);
}