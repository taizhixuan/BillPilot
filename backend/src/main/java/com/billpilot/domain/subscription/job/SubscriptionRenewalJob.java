package com.billpilot.domain.subscription.job;

import com.billpilot.domain.invoice.service.InvoiceService;
import com.billpilot.domain.plan.entity.Plan;
import com.billpilot.domain.plan.repository.PlanRepository;
import com.billpilot.domain.subscription.entity.Subscription;
import com.billpilot.domain.subscription.entity.SubscriptionStatus;
import com.billpilot.domain.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionRenewalJob {

    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;
    private final InvoiceService invoiceService;

    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void renewActiveSubscriptions() {
        Instant now = Instant.now();
        List<Subscription> dueForRenewal = subscriptionRepository.findDueForRenewal(SubscriptionStatus.ACTIVE, now);
        log.info("Found {} subscriptions due for renewal", dueForRenewal.size());

        for (Subscription sub : dueForRenewal) {
            try {
                Plan plan = planRepository.findById(sub.getPlanId()).orElse(null);
                if (plan == null) continue;

                Instant newStart = sub.getCurrentPeriodEnd();
                Instant newEnd = switch (plan.getBillingInterval()) {
                    case MONTHLY -> newStart.plus(30, ChronoUnit.DAYS);
                    case YEARLY -> newStart.plus(365, ChronoUnit.DAYS);
                    case ONE_TIME -> newStart.plus(36500, ChronoUnit.DAYS);
                };

                sub.setCurrentPeriodStart(newStart);
                sub.setCurrentPeriodEnd(newEnd);
                subscriptionRepository.save(sub);

                invoiceService.generateInvoice(sub, plan);
                log.info("Renewed subscription {} for customer {}", sub.getId(), sub.getCustomerId());
            } catch (Exception e) {
                log.error("Failed to renew subscription {}", sub.getId(), e);
            }
        }
    }

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void expireTrials() {
        Instant now = Instant.now();
        List<Subscription> expiredTrials = subscriptionRepository.findExpiredTrials(now);
        log.info("Found {} expired trials", expiredTrials.size());

        for (Subscription sub : expiredTrials) {
            sub.setStatus(SubscriptionStatus.EXPIRED);
            subscriptionRepository.save(sub);
            log.info("Expired trial subscription {}", sub.getId());
        }
    }
}
