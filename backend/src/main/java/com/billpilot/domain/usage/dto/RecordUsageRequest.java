package com.billpilot.domain.usage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.UUID;

@Data
public class RecordUsageRequest {
    @NotNull
    private UUID subscriptionId;

    @NotNull
    private UUID customerId;

    @NotBlank
    private String featureKey;

    @Positive
    private long quantity = 1;
}
