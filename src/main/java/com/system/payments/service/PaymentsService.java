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
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PaymentsService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private LedgerService ledgerService;

    @Transactional
    public Payment addANewPayment(PaymentsRequest paymentRequest) throws Exception{
        Payment payment = new Payment();

        payment.setAmount(paymentRequest.getAmount());
        payment.setCurrency(paymentRequest.getCurrency());
        payment.setDestAccount(paymentRequest.getDestAccount());
        payment.setIdempotencyKey(paymentRequest.getIdempotencyKey());
        payment.setSourceAccount(paymentRequest.getSourceAccount());

        payment.setStatus("CREATED");
        payment.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        payment.setLedgerCreated(false);

        paymentRepository.save(payment);

        return payment;
    }

    public String recordPayment(PaymentsRequest paymentRequest){

        Payment payment=null;
        try {
            Payment prevPayment = checkForDuplicatePayment(paymentRequest);
            if (prevPayment != null) {
                log.info("Duplicate Payment {}", paymentRequest.getSourceAccount());
                return "Duplicate Payment";
            }
            //Payment persistence is atomic
            payment = addANewPayment(paymentRequest);
        }catch(Exception e){
            log.error("Error while trying to add a new payment for id {} - {}",paymentRequest.getSourceAccount(), e.getMessage());
        }

        try{
            //Ledger creation is eventually consistent and retried separately
            ledgerService.addLedgerRecord(payment);
        }catch(Exception e){
            log.error("Error while trying to add ledger entry for {}",e.getMessage());
            payment.setRetryCount(payment.getRetryCount()+1);
            payment.setNextRetryAt(LocalDateTime.now().plusMinutes(1));
            paymentRepository.save(payment);
            return "Ledger failed";
        }

        payment.setLedgerCreated(true);
        paymentRepository.save(payment);
        return "Payment Successful";
    }

    private Payment checkForDuplicatePayment(PaymentsRequest payment){

        String key = payment.getIdempotencyKey();
        Optional<Payment> existing = paymentRepository.findByIdempotencyKey(key);

        return existing.orElse(null);
    }

    public Payment getPayment(Long id){
        return paymentRepository.findById(id).orElse(null);
    }

    public List<Payment> mismatches(){
        List<Payment> mismatched = paymentRepository.findByStatusAndLedgerCreated("CREATED",false);
        return mismatched;
    }
}
