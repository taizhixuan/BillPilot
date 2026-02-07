package com.billpilot.domain.dashboard.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DashboardResponse {
    private BigDecimal mrr;
    private long activeSubscriptions;
    private double churnRate;
    private long overdueInvoices;
    private List<RevenuePoint> revenueTimeline;

    @Data
    @Builder
    public static class RevenuePoint {
        private String date;
        private BigDecimal revenue;
    }
}
