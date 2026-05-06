package com.system.payments.entity;

import com.system.payments.util.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private String currency;

    @NotNull
    private String sourceAccount;

    @NotNull
    private String destAccount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private Boolean ledgerCreated;

    private Integer retryCount=0;

    private LocalDateTime nextRetryAt;

    @NotNull
    @Column(unique=true)
    private String idempotencyKey;

    private Timestamp createdAt;
}
