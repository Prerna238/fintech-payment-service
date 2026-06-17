package com.system.payments.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentsRequest{

    @NotNull
    @Positive
    @Schema(example="1000.00")
    private BigDecimal amount;

    @NotBlank
    @Schema(example="Rupee")
    private String currency;

    @NotBlank
    @Schema(example="974979329")
    private String sourceAccount;

    @NotBlank
    @Schema(example="247979239")
    private String destAccount;

    @NotBlank
    @Schema(example="FASDF345")
    private String idempotencyKey;
}
