package com.anirudh.payments.service;

import com.anirudh.payments.dto.PaymentResponse;
import com.anirudh.payments.entity.Payment;
import com.anirudh.payments.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public Optional<PaymentResponse> getPayment(Long paymentId) {
        log.info("Fetching payment {} from the REST endpoint", paymentId);
        return paymentRepository.findById(paymentId).map(this::toResponse);
    }

    // Helpers
    private PaymentResponse toResponse(Payment p) {
        return new PaymentResponse(p.getPaymentId(), p.getClientId(), p.getAmount(), p.getStatus(), p.getPublishedAt());
    }
}
