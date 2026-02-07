package com.billpilot.domain.invoice.repository;

import com.billpilot.domain.invoice.entity.InvoiceNumberSequence;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface InvoiceNumberSequenceRepository extends JpaRepository<InvoiceNumberSequence, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM InvoiceNumberSequence s WHERE s.orgId = :orgId AND s.year = :year")
    Optional<InvoiceNumberSequence> findByOrgIdAndYearForUpdate(UUID orgId, int year);
}
