package com.anirudh.payments.dto;

import com.anirudh.payments.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentDto(
        Long paymentId,
        String clientId,
        BigDecimal amount,
        PaymentStatus status,
        LocalDateTime publishedAt
) {}