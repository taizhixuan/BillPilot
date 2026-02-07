package com.billpilot.domain.subscription.service;

import com.billpilot.common.dto.PagedResponse;
import com.billpilot.common.exception.BadRequestException;
import com.billpilot.common.exception.ResourceNotFoundException;
import com.billpilot.domain.audit.service.AuditService;
import com.billpilot.domain.customer.entity.Customer;
import com.billpilot.domain.customer.repository.CustomerRepository;
import com.billpilot.domain.invoice.service.InvoiceService;
import com.billpilot.domain.plan.entity.Plan;
import com.billpilot.domain.plan.repository.PlanRepository;
import com.billpilot.domain.subscription.dto.*;
import com.billpilot.domain.subscription.entity.Subscription;
import com.billpilot.domain.subscription.entity.SubscriptionStatus;
import com.billpilot.domain.subscription.mapper.SubscriptionMapper;
import com.billpilot.domain.subscription.repository.SubscriptionRepository;
import com.billpilot.security.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final CustomerRepository customerRepository;
    private final PlanRepository planRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final InvoiceService invoiceService;
    private final ProrationService prorationService;
    private final AuditService auditService;

    @Transactional(readOnly = true)
    public PagedResponse<SubscriptionResponse> listSubscriptions(Pageable pageable) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Page<Subscription> page = subscriptionRepository.findAllByOrgId(orgId, pageable);
        return PagedResponse.of(subscriptionMapper.toResponseList(page.getContent()), page);
    }

    @Transactional(readOnly = true)
    public PagedResponse<SubscriptionResponse> listByCustomer(UUID customerId, Pageable pageable) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Page<Subscription> page = subscriptionRepository.findAllByOrgIdAndCustomerId(orgId, customerId, pageable);
        return PagedResponse.of(subscriptionMapper.toResponseList(page.getContent()), page);
    }

    @Transactional(readOnly = true)
    public SubscriptionResponse getSubscription(UUID subscriptionId) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Subscription sub = subscriptionRepository.findByIdAndOrgId(subscriptionId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", subscriptionId));
        return subscriptionMapper.toResponse(sub);
    }

    @Transactional
    public SubscriptionResponse createSubscription(CreateSubscriptionRequest request) {
        UUID orgId = TenantContext.getCurrentOrgId();

        Customer customer = customerRepository.findByIdAndOrgId(request.getCustomerId(), orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", request.getCustomerId()));
        Plan plan = planRepository.findByIdAndOrgId(request.getPlanId(), orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", request.getPlanId()));

        Instant now = Instant.now();
        Instant periodStart = now;
        Instant periodEnd;
        Instant trialEnd = null;
        SubscriptionStatus status;

        if (request.isStartTrial() && plan.getTrialDays() > 0) {
            status = SubscriptionStatus.TRIALING;
            trialEnd = now.plus(plan.getTrialDays(), ChronoUnit.DAYS);
            periodEnd = trialEnd;
        } else {
            status = SubscriptionStatus.ACTIVE;
            periodEnd = switch (plan.getBillingInterval()) {
                case MONTHLY -> now.plus(30, ChronoUnit.DAYS);
                case YEARLY -> now.plus(365, ChronoUnit.DAYS);
                case ONE_TIME -> now.plus(36500, ChronoUnit.DAYS);
            };
        }

        Subscription sub = Subscription.builder()
                .customerId(customer.getId())
                .planId(plan.getId())
                .status(status)
                .quantity(request.getQuantity())
                .currentPeriodStart(periodStart)
                .currentPeriodEnd(periodEnd)
                .trialEnd(trialEnd)
                .build();
        sub.setOrgId(orgId);
        sub = subscriptionRepository.save(sub);

        if (status == SubscriptionStatus.ACTIVE) {
            invoiceService.generateInvoice(sub, plan);
        }

        auditService.logCurrentUser("CREATE", "Subscription", sub.getId(),
                "Created subscription for " + customer.getName() + " on " + plan.getName());

        sub.setCustomer(customer);
        sub.setPlan(plan);
        return subscriptionMapper.toResponse(sub);
    }

    @Transactional
    public SubscriptionResponse cancelSubscription(UUID subscriptionId) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Subscription sub = subscriptionRepository.findByIdAndOrgId(subscriptionId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", subscriptionId));

        if (sub.getStatus() == SubscriptionStatus.CANCELED) {
            throw new BadRequestException("Subscription is already canceled");
        }

        sub.setStatus(SubscriptionStatus.CANCELED);
        sub.setCanceledAt(Instant.now());
        sub = subscriptionRepository.save(sub);

        auditService.logCurrentUser("CANCEL", "Subscription", sub.getId(), "Canceled subscription");
        return subscriptionMapper.toResponse(sub);
    }

    @Transactional
    public SubscriptionResponse changePlan(UUID subscriptionId, ChangePlanRequest request) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Subscription sub = subscriptionRepository.findByIdAndOrgId(subscriptionId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", subscriptionId));

        if (sub.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new BadRequestException("Can only change plan on active subscriptions");
        }

        Plan oldPlan = planRepository.findByIdAndOrgId(sub.getPlanId(), orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", sub.getPlanId()));
        Plan newPlan = planRepository.findByIdAndOrgId(request.getNewPlanId(), orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", request.getNewPlanId()));

        int newQuantity = request.getNewQuantity() != null ? request.getNewQuantity() : sub.getQuantity();

        BigDecimal credit = prorationService.calculateCredit(sub, oldPlan);
        BigDecimal charge = prorationService.calculateCharge(sub, newPlan, newQuantity);
        BigDecimal netAmount = charge.subtract(credit);

        sub.setPlanId(newPlan.getId());
        sub.setQuantity(newQuantity);
        Subscription savedSub = subscriptionRepository.save(sub);

        if (netAmount.compareTo(BigDecimal.ZERO) > 0) {
            invoiceService.generateProrationInvoice(savedSub, newPlan, netAmount, "Plan change proration");
        }

        auditService.logCurrentUser("CHANGE_PLAN", "Subscription", savedSub.getId(),
                "Changed from " + oldPlan.getName() + " to " + newPlan.getName());

        savedSub.setPlan(newPlan);
        return subscriptionMapper.toResponse(savedSub);
    }
}
