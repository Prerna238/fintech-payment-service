package com.system.payments.service;

import com.system.payments.entity.Payment;
import com.system.payments.model.PaymentsRequest;
import com.system.payments.repository.PaymentRepository;
import jakarta.transaction.Transactional;
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

    @Autowired
    private LedgerService ledgerService;

    @Transactional
    public String recordPayment(PaymentsRequest paymentRequest){

        try {
            Payment prevPayment = checkForDuplicatePayment(paymentRequest);
            if (prevPayment != null) {
                log.info("Duplicate Payment {}", paymentRequest.getSourceAccount());
                return "Duplicate Payment";
            }

            Payment payment = new Payment();

            payment.setAmount(paymentRequest.getAmount());
            payment.setCurrency(paymentRequest.getCurrency());
            payment.setDestAccount(paymentRequest.getDestAccount());
            payment.setIdempotencyKey(paymentRequest.getIdempotencyKey());
            payment.setSourceAccount(paymentRequest.getSourceAccount());

            payment.setStatus("CREATED");
            payment.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

            paymentRepository.save(payment);
            ledgerService.addLedgerRecord(payment);
            return "Payment Successful";
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
