package com.system.payments.model;

import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentsRequest{

    private BigDecimal amount;
    private String currency;
    private String sourceAccount;
    private String destAccount;
    private String idempotencyKey;
}
