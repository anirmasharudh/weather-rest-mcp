package com.anirudh.payments.repository;

import com.anirudh.payments.entity.Payment;
import com.anirudh.payments.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findFirstByClientIdAndStatusOrderByPublishedAtDesc(
            Long clientId, PaymentStatus status);
}