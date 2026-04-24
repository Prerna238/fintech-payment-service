package com.system.payments.service;

import com.system.payments.entity.LedgerEntity;
import com.system.payments.entity.Payment;
import com.system.payments.repository.LedgerEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.system.payments.util.EntryType.CREDIT;
import static com.system.payments.util.EntryType.DEBIT;

@Slf4j
@Service
public class LedgerService {

    @Autowired
    private LedgerEntryRepository ledgerEntryRepository;

    public String addLedgerRecord(Payment payment){
        try {
            if(Math.random()<0.3)
                throw new RuntimeException();
            LedgerEntity credit = new LedgerEntity();

            credit.setAccount(payment.getDestAccount());
            credit.setType(CREDIT.name());
            credit.setCreatedAt(LocalDateTime.now());
            credit.setPaymentId(payment.getId());
            credit.setAmount(payment.getAmount());

            LedgerEntity debit = new LedgerEntity();

            debit.setAccount(payment.getSourceAccount());
            debit.setType(DEBIT.name());
            debit.setCreatedAt(LocalDateTime.now());
            debit.setPaymentId(payment.getId());
            debit.setAmount(payment.getAmount());

            ledgerEntryRepository.save(credit);
            ledgerEntryRepository.save(debit);
            return "Success";
        }catch(Exception e){
            log.error("Error while trying to add entries in Ledger Service - {}",e.getMessage());
            payment.setLedgerCreated(false);
            return "Failure";
        }
    }
}
