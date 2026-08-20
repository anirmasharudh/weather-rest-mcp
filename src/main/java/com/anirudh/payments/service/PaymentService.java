package com.anirudh.payments.service;

import com.anirudh.payments.dto.PaymentDto;
import com.anirudh.payments.entity.Payment;
import com.anirudh.payments.enums.PaymentStatus;
import com.anirudh.payments.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    // Rest methods
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

    // MCP tools
    @McpTool(description = "Gets a payment record by its payment ID",
            annotations = @McpTool.McpAnnotations(readOnlyHint = true, destructiveHint = false,
                    idempotentHint = true, openWorldHint = false))
    public String getPaymentTool(@McpToolParam(description = "Payment ID, e.g. 'pay-001'") Long paymentId) {
        log.info("Fetching payment {} from the MCP tool", paymentId);
        return getPayment(paymentId)
                .map(p -> String.format("Payment %s: client=%s, amount=%s, status=%s, publishedAt=%s",
                        p.paymentId(), p.clientId(), p.amount(), p.status(), p.publishedAt()))
                .orElse("No payment found with ID " + paymentId);
    }

    @McpTool(description = "Creates or updates a payment record",
            annotations = @McpTool.McpAnnotations(readOnlyHint = false, destructiveHint = true,
                    idempotentHint = true, openWorldHint = false))
    public String createOrUpdatePaymentTool(
            @McpToolParam(description = "Payment ID, e.g. 'pay-005'") Long paymentId,
            @McpToolParam(description = "Client ID, e.g. 'client-alice'", required = false) String clientId,
            @McpToolParam(description = "Payment amount", required = false) BigDecimal amount,
            @McpToolParam(description = "Status: COMPLETED, PENDING, or FAILED", required = false) String status) {
        log.info("Creating/updating payment {} from the MCP tool", paymentId);
        PaymentDto dto = new PaymentDto(paymentId, clientId, amount,
                PaymentStatus.valueOf(status.toUpperCase()), LocalDateTime.now());
        PaymentDto saved = saveOrUpdatePayment(dto);
        return "Saved payment " + saved.paymentId() + " with status " + saved.status();
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
