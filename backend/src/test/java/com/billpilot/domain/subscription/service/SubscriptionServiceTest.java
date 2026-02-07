package com.billpilot.domain.subscription.service;

import com.billpilot.common.exception.BadRequestException;
import com.billpilot.domain.audit.service.AuditService;
import com.billpilot.domain.customer.entity.Customer;
import com.billpilot.domain.customer.repository.CustomerRepository;
import com.billpilot.domain.invoice.service.InvoiceService;
import com.billpilot.domain.plan.entity.BillingInterval;
import com.billpilot.domain.plan.entity.Plan;
import com.billpilot.domain.plan.repository.PlanRepository;
import com.billpilot.domain.subscription.dto.CreateSubscriptionRequest;
import com.billpilot.domain.subscription.entity.Subscription;
import com.billpilot.domain.subscription.entity.SubscriptionStatus;
import com.billpilot.domain.subscription.mapper.SubscriptionMapper;
import com.billpilot.domain.subscription.repository.SubscriptionRepository;
import com.billpilot.security.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock private SubscriptionRepository subscriptionRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private PlanRepository planRepository;
    @Mock private SubscriptionMapper subscriptionMapper;
    @Mock private InvoiceService invoiceService;
    @Mock private ProrationService prorationService;
    @Mock private AuditService auditService;
    @InjectMocks private SubscriptionService subscriptionService;

    private UUID orgId;

    @BeforeEach
    void setUp() {
        orgId = UUID.randomUUID();
        TenantContext.setCurrentOrgId(orgId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void cancelSubscription_shouldThrowIfAlreadyCanceled() {
        UUID subId = UUID.randomUUID();
        Subscription sub = Subscription.builder()
                .status(SubscriptionStatus.CANCELED)
                .build();
        sub.setId(subId);

        when(subscriptionRepository.findByIdAndOrgId(subId, orgId)).thenReturn(Optional.of(sub));

        assertThrows(BadRequestException.class, () -> subscriptionService.cancelSubscription(subId));
    }

    @Test
    void createSubscription_withTrial_shouldSetTrialStatus() {
        UUID customerId = UUID.randomUUID();
        UUID planId = UUID.randomUUID();

        Customer customer = Customer.builder().name("Test").build();
        customer.setId(customerId);
        customer.setOrgId(orgId);

        Plan plan = Plan.builder()
                .name("Pro")
                .price(BigDecimal.valueOf(99))
                .billingInterval(BillingInterval.MONTHLY)
                .trialDays(14)
                .build();
        plan.setId(planId);

        when(customerRepository.findByIdAndOrgId(customerId, orgId)).thenReturn(Optional.of(customer));
        when(planRepository.findByIdAndOrgId(planId, orgId)).thenReturn(Optional.of(plan));
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(inv -> {
            Subscription s = inv.getArgument(0);
            s.setId(UUID.randomUUID());
            return s;
        });

        CreateSubscriptionRequest request = new CreateSubscriptionRequest();
        request.setCustomerId(customerId);
        request.setPlanId(planId);
        request.setQuantity(1);
        request.setStartTrial(true);

        subscriptionService.createSubscription(request);

        verify(subscriptionRepository).save(argThat(sub ->
                sub.getStatus() == SubscriptionStatus.TRIALING && sub.getTrialEnd() != null
        ));
        verify(invoiceService, never()).generateInvoice(any(), any());
    }
}
