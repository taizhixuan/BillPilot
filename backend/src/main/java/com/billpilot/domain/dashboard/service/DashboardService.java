package com.billpilot.domain.dashboard.service;

import com.billpilot.domain.dashboard.dto.DashboardResponse;
import com.billpilot.domain.invoice.entity.InvoiceStatus;
import com.billpilot.domain.invoice.repository.InvoiceRepository;
import com.billpilot.domain.subscription.entity.SubscriptionStatus;
import com.billpilot.domain.subscription.repository.SubscriptionRepository;
import com.billpilot.security.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final SubscriptionRepository subscriptionRepository;
    private final InvoiceRepository invoiceRepository;

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {
        UUID orgId = TenantContext.getCurrentOrgId();

        long activeSubs = subscriptionRepository.countByOrgIdAndStatus(orgId, SubscriptionStatus.ACTIVE);
        long canceledSubs = subscriptionRepository.countByOrgIdAndStatus(orgId, SubscriptionStatus.CANCELED);
        long overdueInvoices = invoiceRepository.countByOrgIdAndStatus(orgId, InvoiceStatus.PAST_DUE);
        BigDecimal totalPaid = invoiceRepository.sumPaidByOrgId(orgId);

        double total = activeSubs + canceledSubs;
        double churnRate = total > 0 ? (canceledSubs / total) * 100 : 0;

        // Generate mock revenue timeline for the last 6 months
        List<DashboardResponse.RevenuePoint> timeline = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDate now = LocalDate.now();
        for (int i = 5; i >= 0; i--) {
            LocalDate month = now.minusMonths(i);
            BigDecimal revenue = totalPaid.multiply(BigDecimal.valueOf(0.7 + Math.random() * 0.6))
                    .divide(BigDecimal.valueOf(6), 2, java.math.RoundingMode.HALF_UP);
            timeline.add(DashboardResponse.RevenuePoint.builder()
                    .date(month.format(fmt))
                    .revenue(revenue)
                    .build());
        }

        return DashboardResponse.builder()
                .mrr(totalPaid.divide(BigDecimal.valueOf(Math.max(1, 6)), 2, java.math.RoundingMode.HALF_UP))
                .activeSubscriptions(activeSubs)
                .churnRate(Math.round(churnRate * 100.0) / 100.0)
                .overdueInvoices(overdueInvoices)
                .revenueTimeline(timeline)
                .build();
    }
}
