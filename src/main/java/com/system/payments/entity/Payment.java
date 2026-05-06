package com.system.payments.entity;

import com.system.payments.util.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue
    private Long id;

    private BigDecimal amount;
    private String currency;
    private String sourceAccount;
    private String destAccount;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private Boolean ledgerCreated;
    private Integer retryCount=0;
    private LocalDateTime nextRetryAt;

    @Column(unique=true)
    private String idempotencyKey;

    private Timestamp createdAt;
}
