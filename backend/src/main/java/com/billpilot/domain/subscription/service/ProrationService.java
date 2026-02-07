package com.billpilot.domain.subscription.service;

import com.billpilot.domain.plan.entity.Plan;
import com.billpilot.domain.subscription.entity.Subscription;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;

@Service
public class ProrationService {

    public BigDecimal calculateCredit(Subscription subscription, Plan oldPlan) {
        Instant now = Instant.now();
        if (now.isAfter(subscription.getCurrentPeriodEnd())) {
            return BigDecimal.ZERO;
        }

        long totalDays = Duration.between(subscription.getCurrentPeriodStart(), subscription.getCurrentPeriodEnd()).toDays();
        long remainingDays = Duration.between(now, subscription.getCurrentPeriodEnd()).toDays();

        if (totalDays <= 0) return BigDecimal.ZERO;

        BigDecimal dailyRate = oldPlan.getPrice()
                .multiply(BigDecimal.valueOf(subscription.getQuantity()))
                .divide(BigDecimal.valueOf(totalDays), 4, RoundingMode.HALF_UP);

        return dailyRate.multiply(BigDecimal.valueOf(remainingDays)).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateCharge(Subscription subscription, Plan newPlan, int newQuantity) {
        Instant now = Instant.now();
        if (now.isAfter(subscription.getCurrentPeriodEnd())) {
            return newPlan.getPrice().multiply(BigDecimal.valueOf(newQuantity));
        }

        long totalDays = Duration.between(subscription.getCurrentPeriodStart(), subscription.getCurrentPeriodEnd()).toDays();
        long remainingDays = Duration.between(now, subscription.getCurrentPeriodEnd()).toDays();

        if (totalDays <= 0) return BigDecimal.ZERO;

        BigDecimal dailyRate = newPlan.getPrice()
                .multiply(BigDecimal.valueOf(newQuantity))
                .divide(BigDecimal.valueOf(totalDays), 4, RoundingMode.HALF_UP);

        return dailyRate.multiply(BigDecimal.valueOf(remainingDays)).setScale(2, RoundingMode.HALF_UP);
    }
}
