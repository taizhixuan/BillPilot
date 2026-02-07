package com.billpilot.domain.plan.dto;

import com.billpilot.domain.plan.entity.BillingInterval;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
public class PlanResponse {
    private UUID id;
    private UUID orgId;
    private String name;
    private String description;
    private BigDecimal price;
    private BillingInterval billingInterval;
    private int trialDays;
    private String features;
    private boolean active;
    private Integer maxSeats;
    private Long usageLimit;
    private Instant createdAt;
}
