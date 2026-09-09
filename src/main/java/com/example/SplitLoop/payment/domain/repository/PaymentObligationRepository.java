package com.example.SplitLoop.payment.domain.repository;

import com.example.SplitLoop.payment.domain.entity.PaymentObligation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentObligationRepository extends JpaRepository<PaymentObligation, UUID> {

}