package com.billpilot.domain.audit.repository;

import com.billpilot.domain.audit.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    Page<AuditLog> findAllByOrgIdOrderByCreatedAtDesc(UUID orgId, Pageable pageable);
    Page<AuditLog> findAllByOrgIdAndEntityTypeOrderByCreatedAtDesc(UUID orgId, String entityType, Pageable pageable);
    Page<AuditLog> findAllByOrgIdAndEntityIdOrderByCreatedAtDesc(UUID orgId, UUID entityId, Pageable pageable);
}
