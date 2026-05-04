package com.system.payments.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
    private String status;
    private Boolean ledgerCreated;
    private Integer retryCount=0;
    private LocalDateTime nextRetryAt;

    @Column(unique=true)
    private String idempotencyKey;

    private Timestamp createdAt;
}
