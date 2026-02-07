package com.billpilot.domain.invoice.repository;

import com.billpilot.domain.invoice.entity.Invoice;
import com.billpilot.domain.invoice.entity.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    @EntityGraph(attributePaths = {"customer", "lineItems"})
    Optional<Invoice> findByIdAndOrgId(UUID id, UUID orgId);

    Page<Invoice> findAllByOrgId(UUID orgId, Pageable pageable);
    Page<Invoice> findAllByOrgIdAndCustomerId(UUID orgId, UUID customerId, Pageable pageable);
    long countByOrgIdAndStatus(UUID orgId, InvoiceStatus status);

    @Query("SELECT COALESCE(SUM(i.total), 0) FROM Invoice i WHERE i.orgId = :orgId AND i.status = 'PAID'")
    BigDecimal sumPaidByOrgId(UUID orgId);

    @Query("SELECT i FROM Invoice i WHERE i.status = 'OPEN' AND i.dueDate < :today")
    List<Invoice> findOverdueInvoices(LocalDate today);
}
