package com.system.payments.service;

import com.system.payments.entity.Payment;
import com.system.payments.model.PaymentsRequest;
import com.system.payments.repository.PaymentRepository;
import com.system.payments.util.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentsService paymentsService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private LedgerService ledgerService;

    @Test
    void shouldCreatePaymentSuccessfully() throws Exception {

        PaymentsRequest request = new PaymentsRequest();
        request.setAmount(BigDecimal.valueOf(100));
        request.setCurrency("INR");
        request.setSourceAccount("SRC");
        request.setDestAccount("DST");
        request.setIdempotencyKey("KEY1");

        when(paymentRepository.findByIdempotencyKey("KEY1"))
                .thenReturn(Optional.empty());

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String result = paymentsService.recordPayment(request);

        assertEquals("Payment Successful", result);

        verify(paymentRepository, atLeastOnce())
                .save(any(Payment.class));

        verify(ledgerService)
                .addLedgerRecord(any(Payment.class));
    }

    @Test
    void shouldRejectDuplicatePayment() throws Exception {

        PaymentsRequest request = new PaymentsRequest();
        request.setIdempotencyKey("DUPLICATE");

        Payment existing = new Payment();

        when(paymentRepository.findByIdempotencyKey("DUPLICATE"))
                .thenReturn(Optional.of(existing));

        String result = paymentsService.recordPayment(request);

        assertEquals("Duplicate Payment", result);

        verify(ledgerService, never())
                .addLedgerRecord(any());
    }

    @Test
    void shouldMarkPaymentForRetryWhenLedgerFails() throws Exception {

        PaymentsRequest request = new PaymentsRequest();

        request.setAmount(BigDecimal.valueOf(100));
        request.setCurrency("INR");
        request.setSourceAccount("SRC");
        request.setDestAccount("DST");
        request.setIdempotencyKey("KEY2");

        when(paymentRepository.findByIdempotencyKey("KEY2"))
                .thenReturn(Optional.empty());

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        doThrow(new RuntimeException("Ledger Failure"))
                .when(ledgerService)
                .addLedgerRecord(any());

        String result = paymentsService.recordPayment(request);

        assertEquals("Ledger failed", result);

        verify(paymentRepository, atLeast(2))
                .save(any(Payment.class));
    }

    @Test
    void shouldReturnPaymentById() {

        Payment payment = new Payment();
        payment.setId(1L);

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.of(payment));

        Payment result = paymentsService.getPayment(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void shouldReturnMismatchedPayments() {

        List<Payment> payments =
                List.of(new Payment(), new Payment());

        when(paymentRepository.findByStatusAndLedgerCreated(
                PaymentStatus.SUCCESS,
                false))
                .thenReturn(payments);

        List<Payment> result =
                paymentsService.mismatches();

        assertEquals(2, result.size());
    }
}