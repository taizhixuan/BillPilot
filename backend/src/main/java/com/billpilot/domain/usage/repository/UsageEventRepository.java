package com.billpilot.domain.usage.repository;

import com.billpilot.domain.usage.entity.UsageEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.UUID;

public interface UsageEventRepository extends JpaRepository<UsageEvent, UUID> {
    Page<UsageEvent> findAllByOrgIdAndSubscriptionId(UUID orgId, UUID subscriptionId, Pageable pageable);
    Page<UsageEvent> findAllByOrgIdAndCustomerId(UUID orgId, UUID customerId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(u.quantity), 0) FROM UsageEvent u " +
           "WHERE u.subscriptionId = :subscriptionId AND u.featureKey = :featureKey " +
           "AND u.recordedAt >= :since")
    long sumUsage(UUID subscriptionId, String featureKey, Instant since);
}
