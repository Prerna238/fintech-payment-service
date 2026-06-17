package com.system.payments.controller;

import com.system.payments.entity.Payment;
import com.system.payments.service.PaymentsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(PaymentsController.class)
class PaymentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentsService paymentsService;

    @Test
    void shouldCreatePayment() throws Exception {

        when(paymentsService.recordPayment(any()))
                .thenReturn("Payment Successful");

        mockMvc.perform(
                        post("/newPayment")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                          "amount":100,
                          "currency":"INR",
                          "sourceAccount":"SRC",
                          "destAccount":"DST",
                          "idempotencyKey":"KEY"
                        }
                    """)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnConflictForDuplicatePayment() throws Exception {

        when(paymentsService.recordPayment(any()))
                .thenReturn("Duplicate Payment");

        mockMvc.perform(
                        post("/newPayment")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                          "amount":100,
                          "currency":"INR",
                          "sourceAccount":"SRC",
                          "destAccount":"DST",
                          "idempotencyKey":"KEY"
                        }
                    """)
                )
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnMismatches() throws Exception {

        when(paymentsService.mismatches())
                .thenReturn(List.of(new Payment()));

        mockMvc.perform(
                        get("/reconcilliation/mismatch")
                )
                .andExpect(status().isOk());
    }
}