package com.billpilot.domain.payment.dto;

import com.billpilot.domain.payment.entity.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
public class PaymentResponse {
    private UUID id;
    private UUID orgId;
    private UUID invoiceId;
    private String invoiceNumber;
    private UUID customerId;
    private String customerName;
    private BigDecimal amount;
    private PaymentStatus status;
    private String paymentMethod;
    private String transactionId;
    private String failureReason;
    private Instant createdAt;
}
