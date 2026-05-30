package com.system.payments.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck{

    @Operation(summary="healthCheck", description= "Checking the application health")
    @GetMapping("health")
    public String healthCheck(){
        return "Payment System is running";
    }

}