package com.system.payments.service;

import com.system.payments.entity.Payment;
import com.system.payments.model.PaymentsRequest;
import com.system.payments.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class PaymentsService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment recordPayment(PaymentsRequest paymentRequest){

        try {
            Payment prevPayment = checkForDuplicatePayment(paymentRequest);
            if (prevPayment != null) {
                log.info("Payment was already initiated for {}", paymentRequest.getSourceAccount());
                return prevPayment;
            }

            Payment payment = new Payment();

            payment.setAmount(paymentRequest.getAmount());
            payment.setCurrency(paymentRequest.getCurrency());
            payment.setDestAccount(paymentRequest.getDestAccount());
            payment.setIdempotencyKey(paymentRequest.getIdempotencyKey());
            payment.setSourceAccount(paymentRequest.getSourceAccount());

            payment.setStatus("CREATED");
            payment.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

            return paymentRepository.save(payment);
        }catch(Exception e){
            log.error("Error while initiating payment for {}", paymentRequest.getSourceAccount());
        }
        return null;
    }

    private Payment checkForDuplicatePayment(PaymentsRequest payment){

        String key = payment.getIdempotencyKey();
        Optional<Payment> existing = paymentRepository.findByIdempotencyKey(key);

        return existing.orElse(null);
    }

    public Payment getPayment(Long id){
        return paymentRepository.findById(id).orElse(null);
    }

}
