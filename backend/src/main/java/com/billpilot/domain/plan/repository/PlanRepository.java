package com.billpilot.domain.plan.repository;

import com.billpilot.domain.plan.entity.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan, UUID> {
    Page<Plan> findAllByOrgId(UUID orgId, Pageable pageable);
    Page<Plan> findAllByOrgIdAndActive(UUID orgId, boolean active, Pageable pageable);
    Optional<Plan> findByIdAndOrgId(UUID id, UUID orgId);
    boolean existsByNameAndOrgId(String name, UUID orgId);
}
