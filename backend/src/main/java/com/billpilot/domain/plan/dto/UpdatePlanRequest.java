package com.billpilot.domain.plan.dto;

import com.billpilot.domain.plan.entity.BillingInterval;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdatePlanRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private BillingInterval billingInterval;
    private Integer trialDays;
    private String features;
    private Boolean active;
    private Integer maxSeats;
    private Long usageLimit;
}
