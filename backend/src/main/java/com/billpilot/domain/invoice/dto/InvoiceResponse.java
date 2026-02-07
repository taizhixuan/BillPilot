package com.billpilot.domain.invoice.dto;

import com.billpilot.domain.invoice.entity.InvoiceStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class InvoiceResponse {
    private UUID id;
    private UUID orgId;
    private UUID customerId;
    private String customerName;
    private UUID subscriptionId;
    private String invoiceNumber;
    private InvoiceStatus status;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;
    private LocalDate dueDate;
    private Instant paidAt;
    private List<LineItemResponse> lineItems;
    private Instant createdAt;

    @Data
    public static class LineItemResponse {
        private UUID id;
        private String description;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal amount;
    }
}
