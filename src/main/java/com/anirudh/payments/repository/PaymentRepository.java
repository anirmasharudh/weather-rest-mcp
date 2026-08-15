package com.anirudh.payments.repository;

import com.anirudh.payments.entity.Payment;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@ConditionalOnProperty(value = "app.db-enabled", havingValue = "true")
public interface PaymentRepository extends JpaRepository<Payment, String> {

    Optional<Payment> findFirstByClientIdAndStatusOrderByPublishedAtDesc(
            String clientId, String status);
}
