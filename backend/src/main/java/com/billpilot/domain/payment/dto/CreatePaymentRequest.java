package com.billpilot.domain.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreatePaymentRequest {
    @NotNull
    private UUID invoiceId;

    private String paymentMethod = "card";
}
