package com.billpilot.domain.subscription.service;

import com.billpilot.domain.plan.entity.Plan;
import com.billpilot.domain.subscription.entity.Subscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class ProrationServiceTest {

    private ProrationService prorationService;

    @BeforeEach
    void setUp() {
        prorationService = new ProrationService();
    }

    @Test
    void calculateCredit_shouldReturnProRatedCredit() {
        Instant start = Instant.now().minus(15, ChronoUnit.DAYS);
        Instant end = Instant.now().plus(15, ChronoUnit.DAYS);

        Subscription sub = Subscription.builder()
                .currentPeriodStart(start)
                .currentPeriodEnd(end)
                .quantity(1)
                .build();

        Plan plan = Plan.builder().price(BigDecimal.valueOf(30)).build();

        BigDecimal credit = prorationService.calculateCredit(sub, plan);
        assertTrue(credit.compareTo(BigDecimal.ZERO) > 0);
        assertTrue(credit.compareTo(BigDecimal.valueOf(30)) < 0);
    }

    @Test
    void calculateCredit_shouldReturnZeroForExpiredPeriod() {
        Instant start = Instant.now().minus(30, ChronoUnit.DAYS);
        Instant end = Instant.now().minus(1, ChronoUnit.DAYS);

        Subscription sub = Subscription.builder()
                .currentPeriodStart(start)
                .currentPeriodEnd(end)
                .quantity(1)
                .build();

        Plan plan = Plan.builder().price(BigDecimal.valueOf(30)).build();

        BigDecimal credit = prorationService.calculateCredit(sub, plan);
        assertEquals(0, credit.compareTo(BigDecimal.ZERO));
    }

    @Test
    void calculateCharge_shouldReturnProRatedCharge() {
        Instant start = Instant.now().minus(15, ChronoUnit.DAYS);
        Instant end = Instant.now().plus(15, ChronoUnit.DAYS);

        Subscription sub = Subscription.builder()
                .currentPeriodStart(start)
                .currentPeriodEnd(end)
                .quantity(1)
                .build();

        Plan plan = Plan.builder().price(BigDecimal.valueOf(60)).build();

        BigDecimal charge = prorationService.calculateCharge(sub, plan, 2);
        assertTrue(charge.compareTo(BigDecimal.ZERO) > 0);
    }
}
