package com.system.payments.scheduler;

import com.system.payments.entity.Payment;
import com.system.payments.repository.LedgerEntryRepository;
import com.system.payments.repository.PaymentRepository;
import com.system.payments.service.LedgerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static com.system.payments.util.Constants.MAX_RETRIES;

@Slf4j
@EnableScheduling
@RestController
public class LedgerReprocessScheduler {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private LedgerEntryRepository ledgerEntryRepository;

    @Autowired
    private LedgerService ledgerService;

    @Scheduled(fixedRate = 30000)
    public void reprocessFailedLedger(){

        log.info("Started Scheduler for failed Ledgers");
        try {
            List<Payment> successfulPayments = paymentRepository.findByStatusAndLedgerCreatedAndNextRetryAtBefore("CREATED",false, LocalDateTime.now());
            log.info("Found {} cases of failed Ledger",successfulPayments.size());
            for (Payment payment : successfulPayments) {
                if(payment.getRetryCount()>=MAX_RETRIES)
                    payment.setStatus("FAILED");
                else {
                    String res = ledgerService.addLedgerRecord(payment);
                    if (res.equals("Success")) {
                        payment.setLedgerCreated(true);
                        paymentRepository.save(payment);
                    }
                    else{
                        payment.setRetryCount(payment.getRetryCount()+1);
                        payment.setNextRetryAt(LocalDateTime.now().plusMinutes(1));
                    }
                }
                paymentRepository.save(payment);
            }
        }catch (Exception e){
            log.error("Error while trying to reprocess failed Ledger {}",e.getMessage());
        }
    }
}
