package com.anirudh.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private String paymentId;
    private String clientId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime publishedAt;
    private final boolean degraded = false; // true when served from the fallback, not the live path
}
