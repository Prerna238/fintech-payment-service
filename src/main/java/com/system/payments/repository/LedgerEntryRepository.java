package com.system.payments.repository;

import com.system.payments.entity.LedgerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntity,Long> {
    List<LedgerEntity> findByPaymentId(Long paymentId);
}
