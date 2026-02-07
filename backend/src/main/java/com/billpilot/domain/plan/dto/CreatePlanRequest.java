package com.billpilot.domain.plan.dto;

import com.billpilot.domain.plan.entity.BillingInterval;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePlanRequest {
    @NotBlank
    private String name;

    private String description;

    @NotNull @Positive
    private BigDecimal price;

    @NotNull
    private BillingInterval billingInterval;

    private int trialDays = 0;
    private String features;
    private Integer maxSeats;
    private Long usageLimit;
}
