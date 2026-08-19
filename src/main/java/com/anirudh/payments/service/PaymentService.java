package com.anirudh.payments.service;

import com.anirudh.payments.dto.PaymentDto;
import com.anirudh.payments.entity.Payment;
import com.anirudh.payments.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public Optional<PaymentDto> getPayment(Long paymentId) {
        log.info("Fetching payment {} from the REST endpoint", paymentId);
        return paymentRepository.findById(paymentId).map(this::toDto);
    }

    public List<PaymentDto> getPayments() {
        log.info("Fetching all payments from the REST endpoint");
        return paymentRepository.findAll().stream().map(this::toDto).toList();
    }

    public PaymentDto saveOrUpdatePayment(PaymentDto dto) {
        log.info("Saving payment {} from the REST endpoint", dto.paymentId());
        Payment saved = paymentRepository.save(toEntity(dto));
        return toDto(saved);
    }

    // Helpers
    private PaymentDto toDto(Payment p) {
        return new PaymentDto(p.getPaymentId(), p.getClientId(), p.getAmount(), p.getStatus(), p.getPublishedAt());
    }

    private Payment toEntity(PaymentDto dto) {
        Payment payment = new Payment();
        payment.setPaymentId(dto.paymentId());
        payment.setClientId(dto.clientId());
        payment.setAmount(dto.amount());
        payment.setStatus(dto.status());
        payment.setPublishedAt(dto.publishedAt() != null ? dto.publishedAt() : LocalDateTime.now());
        return payment;
    }
}
