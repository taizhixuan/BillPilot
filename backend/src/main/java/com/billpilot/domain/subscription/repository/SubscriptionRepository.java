package com.billpilot.domain.subscription.repository;

import com.billpilot.domain.subscription.entity.Subscription;
import com.billpilot.domain.subscription.entity.SubscriptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    Optional<Subscription> findByIdAndOrgId(UUID id, UUID orgId);
    Page<Subscription> findAllByOrgId(UUID orgId, Pageable pageable);
    Page<Subscription> findAllByOrgIdAndCustomerId(UUID orgId, UUID customerId, Pageable pageable);
    long countByOrgIdAndStatus(UUID orgId, SubscriptionStatus status);

    @Query("SELECT s FROM Subscription s WHERE s.status = :status AND s.currentPeriodEnd <= :now")
    List<Subscription> findDueForRenewal(SubscriptionStatus status, Instant now);

    @Query("SELECT s FROM Subscription s WHERE s.status = :status AND s.trialEnd <= :now")
    List<Subscription> findExpiredTrials(@Param("status") SubscriptionStatus status, @Param("now") Instant now);
}
