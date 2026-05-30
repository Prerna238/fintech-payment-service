package com.system.payments.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentsRequest{

    @Schema(example="1000.00")
    private BigDecimal amount;

    @Schema(example="Rupee")
    private String currency;

    @Schema(example="974979329")
    private String sourceAccount;

    @Schema(example="247979239")
    private String destAccount;

    @Schema(example="FASDF345")
    private String idempotencyKey;
}
