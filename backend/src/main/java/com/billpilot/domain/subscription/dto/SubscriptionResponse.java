package com.billpilot.domain.subscription.dto;

import com.billpilot.domain.subscription.entity.SubscriptionStatus;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class SubscriptionResponse {
    private UUID id;
    private UUID orgId;
    private UUID customerId;
    private String customerName;
    private UUID planId;
    private String planName;
    private SubscriptionStatus status;
    private int quantity;
    private Instant currentPeriodStart;
    private Instant currentPeriodEnd;
    private Instant trialEnd;
    private Instant canceledAt;
    private Instant createdAt;
}
