package com.billpilot.domain.auth.repository;

import com.billpilot.domain.auth.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    Optional<Organization> findBySlug(String slug);
    boolean existsBySlug(String slug);
    boolean existsByOrgCode(String orgCode);
}
