package com.billpilot.domain.subscription.job;

import com.billpilot.domain.invoice.service.InvoiceService;
import com.billpilot.domain.plan.entity.BillingInterval;
import com.billpilot.domain.plan.entity.Plan;
import com.billpilot.domain.plan.repository.PlanRepository;
import com.billpilot.domain.subscription.entity.Subscription;
import com.billpilot.domain.subscription.entity.SubscriptionStatus;
import com.billpilot.domain.subscription.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionRenewalJobTest {

    @Mock private SubscriptionRepository subscriptionRepository;
    @Mock private PlanRepository planRepository;
    @Mock private InvoiceService invoiceService;
    @InjectMocks private SubscriptionRenewalJob renewalJob;

    @Test
    void renewActiveSubscriptions_shouldRenewAndGenerateInvoice() {
        UUID planId = UUID.randomUUID();
        Subscription sub = Subscription.builder()
                .planId(planId)
                .customerId(UUID.randomUUID())
                .status(SubscriptionStatus.ACTIVE)
                .currentPeriodStart(Instant.now().minus(30, ChronoUnit.DAYS))
                .currentPeriodEnd(Instant.now().minus(1, ChronoUnit.DAYS))
                .quantity(1)
                .build();
        sub.setId(UUID.randomUUID());
        sub.setOrgId(UUID.randomUUID());

        Plan plan = Plan.builder()
                .name("Starter")
                .price(BigDecimal.valueOf(29))
                .billingInterval(BillingInterval.MONTHLY)
                .build();

        when(subscriptionRepository.findDueForRenewal(eq(SubscriptionStatus.ACTIVE), any()))
                .thenReturn(List.of(sub));
        when(planRepository.findById(planId)).thenReturn(Optional.of(plan));

        renewalJob.renewActiveSubscriptions();

        verify(subscriptionRepository).save(sub);
        verify(invoiceService).generateInvoice(sub, plan);
    }

    @Test
    void expireTrials_shouldSetExpiredStatus() {
        Subscription sub = Subscription.builder()
                .status(SubscriptionStatus.TRIALING)
                .trialEnd(Instant.now().minus(1, ChronoUnit.DAYS))
                .build();
        sub.setId(UUID.randomUUID());

        when(subscriptionRepository.findExpiredTrials(any())).thenReturn(List.of(sub));

        renewalJob.expireTrials();

        verify(subscriptionRepository).save(argThat(s -> s.getStatus() == SubscriptionStatus.EXPIRED));
    }
}
