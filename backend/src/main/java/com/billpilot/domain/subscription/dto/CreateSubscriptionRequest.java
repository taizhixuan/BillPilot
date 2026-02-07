package com.billpilot.domain.subscription.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateSubscriptionRequest {
    @NotNull
    private UUID customerId;

    @NotNull
    private UUID planId;

    @Positive
    private int quantity = 1;

    private boolean startTrial = false;
}
