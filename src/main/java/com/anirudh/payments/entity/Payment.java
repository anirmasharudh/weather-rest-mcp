package com.anirudh.payments.entity;

import com.anirudh.payments.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    private Long paymentId;

    private String clientId;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private LocalDateTime publishedAt;
}
