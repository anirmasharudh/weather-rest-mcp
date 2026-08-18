package com.anirudh.payments.repository;

import com.anirudh.payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    Optional<Payment> findFirstByClientIdAndStatusOrderByPublishedAtDesc(
            String clientId, String status);
}
