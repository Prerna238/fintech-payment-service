package com.system.payments.repository;

import com.system.payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    public Optional<Payment> findByIdempotencyKey(String key);

    @Query(value = "SELECT p from Payment p where p.status=:status and p.ledgerCreated=:ledgerCreated")
    public List<Payment> findByStatusAndLedgerCreated(@Param("status")String status, @Param("ledgerCreated") Boolean ledgerCreated);

    @Query(value = "SELECT p from Payment p where p.status=:status and p.nextRetryAt<=:nextRetryAt")
    public List<Payment> findByStatusRetryPendingAndNextRetryAtBefore(@Param("status")String status, @Param("nextRetryAt") LocalDateTime nextRetryAt);
}
