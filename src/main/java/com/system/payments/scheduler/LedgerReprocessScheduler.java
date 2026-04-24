package com.system.payments.scheduler;

import com.system.payments.entity.Payment;
import com.system.payments.repository.LedgerEntryRepository;
import com.system.payments.repository.PaymentRepository;
import com.system.payments.service.LedgerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
@EnableScheduling
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
            List<Payment> successfulPayments = paymentRepository.findByStatusAndLedgerCreated("CREATED", true);
            log.info("Found {} cases of failed Ledger",successfulPayments.size());
            for (Payment payment : successfulPayments) {
                ledgerService.addLedgerRecord(payment);
            }
        }catch (Exception e){
            log.error("Error while trying to reprocess failed Ledger {}",e.getMessage());
        }
    }
}
