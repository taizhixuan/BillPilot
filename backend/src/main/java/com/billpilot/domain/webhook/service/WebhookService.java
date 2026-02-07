package com.billpilot.domain.webhook.service;

import com.billpilot.common.dto.PagedResponse;
import com.billpilot.domain.webhook.dto.WebhookEventResponse;
import com.billpilot.domain.webhook.entity.WebhookEvent;
import com.billpilot.domain.webhook.entity.WebhookEventStatus;
import com.billpilot.domain.webhook.mapper.WebhookMapper;
import com.billpilot.domain.webhook.repository.WebhookEventRepository;
import com.billpilot.security.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {

    private final WebhookEventRepository repository;
    private final WebhookMapper mapper;

    @Transactional(readOnly = true)
    public PagedResponse<WebhookEventResponse> listEvents(Pageable pageable) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Page<WebhookEvent> page = repository.findAllByOrgIdOrderByCreatedAtDesc(orgId, pageable);
        return PagedResponse.of(mapper.toResponseList(page.getContent()), page);
    }

    @Async
    @Transactional
    public void dispatchEvent(UUID orgId, String eventType, String payload, String idempotencyKey) {
        if (idempotencyKey != null && repository.existsByIdempotencyKey(idempotencyKey)) {
            log.info("Duplicate webhook event with key {}, skipping", idempotencyKey);
            return;
        }

        WebhookEvent event = WebhookEvent.builder()
                .eventType(eventType)
                .payload(payload)
                .status(WebhookEventStatus.PENDING)
                .idempotencyKey(idempotencyKey)
                .build();
        event.setOrgId(orgId);
        event = repository.save(event);

        // Simulate webhook delivery
        try {
            event.setAttempts(event.getAttempts() + 1);
            event.setLastAttemptAt(Instant.now());
            event.setStatus(WebhookEventStatus.DELIVERED);
            repository.save(event);
            log.info("Webhook event {} delivered successfully", event.getId());
        } catch (Exception e) {
            event.setStatus(WebhookEventStatus.FAILED);
            repository.save(event);
            log.error("Webhook event {} delivery failed", event.getId(), e);
        }
    }
}
