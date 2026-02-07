package com.billpilot.domain.customer.repository;

import com.billpilot.domain.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Page<Customer> findAllByOrgId(UUID orgId, Pageable pageable);
    Optional<Customer> findByIdAndOrgId(UUID id, UUID orgId);
    boolean existsByEmailAndOrgId(String email, UUID orgId);
    long countByOrgId(UUID orgId);

    @Query("SELECT c FROM Customer c WHERE c.orgId = :orgId AND " +
           "(LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.company) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Customer> searchByOrgId(UUID orgId, String search, Pageable pageable);
}
