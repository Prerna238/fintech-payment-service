package com.system.payments.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class LedgerEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Long paymentId;
    private String account;
    private BigDecimal amount;
    private String type;
    private LocalDateTime createdAt;
}


