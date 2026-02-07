package com.billpilot.domain.auth.repository;

import com.billpilot.domain.auth.entity.OrgSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrgSettingsRepository extends JpaRepository<OrgSettings, UUID> {
    Optional<OrgSettings> findByOrgId(UUID orgId);
}
