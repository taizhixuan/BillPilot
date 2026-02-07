package com.billpilot.domain.subscription.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ChangePlanRequest {
    @NotNull
    private UUID newPlanId;
    private Integer newQuantity;
}
