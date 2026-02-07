package com.billpilot.domain.usage.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class UsageEventResponse {
    private UUID id;
    private UUID orgId;
    private UUID subscriptionId;
    private UUID customerId;
    private String featureKey;
    private long quantity;
    private Instant recordedAt;
}
