package com.system.payments.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

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

    @Column(unique=true)
    private String idempotencyKey;

    private Timestamp createdAt;
}
