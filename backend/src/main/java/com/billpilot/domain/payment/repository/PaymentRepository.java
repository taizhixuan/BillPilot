package com.billpilot.domain.payment.repository;

import com.billpilot.domain.payment.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Page<Payment> findAllByOrgId(UUID orgId, Pageable pageable);
    Page<Payment> findAllByOrgIdAndCustomerId(UUID orgId, UUID customerId, Pageable pageable);
    Optional<Payment> findByIdAndOrgId(UUID id, UUID orgId);
}
