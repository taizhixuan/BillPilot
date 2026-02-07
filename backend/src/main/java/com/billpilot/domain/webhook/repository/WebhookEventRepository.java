package com.billpilot.domain.webhook.repository;

import com.billpilot.domain.webhook.entity.WebhookEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WebhookEventRepository extends JpaRepository<WebhookEvent, UUID> {
    Page<WebhookEvent> findAllByOrgIdOrderByCreatedAtDesc(UUID orgId, Pageable pageable);
    boolean existsByIdempotencyKey(String idempotencyKey);
}
