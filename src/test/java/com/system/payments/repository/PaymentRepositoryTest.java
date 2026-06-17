package com.system.payments.repository;

import com.system.payments.entity.Payment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    void shouldFindByIdempotencyKey() {

        Payment payment = new Payment();

        payment.setIdempotencyKey("TEST_KEY");
        payment.setAmount(BigDecimal.TEN);
        payment.setCurrency("INR");
        payment.setSourceAccount("SRC");
        payment.setDestAccount("DST");

        paymentRepository.save(payment);

        Optional<Payment> result =
                paymentRepository.findByIdempotencyKey("TEST_KEY");

        assertTrue(result.isPresent());
    }
}