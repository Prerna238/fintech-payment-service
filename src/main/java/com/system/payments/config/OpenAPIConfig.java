package com.system.payments.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI paymentsConfig() {
        return new OpenAPI().info(new Info().title("FinTech Payment Service")
                .description("Payment processing system with idempotency, ledger consistency, retry orchestration and reconciliation.")
                .version("v1.1"));
    }
}
