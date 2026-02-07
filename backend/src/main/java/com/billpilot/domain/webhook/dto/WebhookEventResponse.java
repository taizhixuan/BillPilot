package com.billpilot.domain.webhook.dto;

import com.billpilot.domain.webhook.entity.WebhookEventStatus;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class WebhookEventResponse {
    private UUID id;
    private UUID orgId;
    private String eventType;
    private String payload;
    private WebhookEventStatus status;
    private int attempts;
    private Instant lastAttemptAt;
    private Instant createdAt;
}
