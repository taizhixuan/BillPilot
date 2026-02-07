package com.billpilot.domain.usage.service;

import com.billpilot.common.dto.PagedResponse;
import com.billpilot.common.exception.BadRequestException;
import com.billpilot.common.exception.ResourceNotFoundException;
import com.billpilot.domain.plan.entity.Plan;
import com.billpilot.domain.plan.repository.PlanRepository;
import com.billpilot.domain.subscription.entity.Subscription;
import com.billpilot.domain.subscription.repository.SubscriptionRepository;
import com.billpilot.domain.usage.dto.*;
import com.billpilot.domain.usage.entity.UsageEvent;
import com.billpilot.domain.usage.mapper.UsageMapper;
import com.billpilot.domain.usage.repository.UsageEventRepository;
import com.billpilot.security.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsageService {

    private final UsageEventRepository usageRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;
    private final UsageMapper usageMapper;

    @Transactional
    public UsageEventResponse recordUsage(RecordUsageRequest request) {
        UUID orgId = TenantContext.getCurrentOrgId();

        Subscription sub = subscriptionRepository.findByIdAndOrgId(request.getSubscriptionId(), orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", request.getSubscriptionId()));

        Plan plan = planRepository.findByIdAndOrgId(sub.getPlanId(), orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", sub.getPlanId()));

        if (plan.getUsageLimit() != null) {
            long currentUsage = usageRepository.sumUsage(
                    sub.getId(), request.getFeatureKey(), sub.getCurrentPeriodStart());
            if (currentUsage + request.getQuantity() > plan.getUsageLimit()) {
                throw new BadRequestException("Usage limit exceeded. Current: " + currentUsage +
                        ", Limit: " + plan.getUsageLimit());
            }
        }

        UsageEvent event = UsageEvent.builder()
                .subscriptionId(request.getSubscriptionId())
                .customerId(request.getCustomerId())
                .featureKey(request.getFeatureKey())
                .quantity(request.getQuantity())
                .build();
        event.setOrgId(orgId);
        event = usageRepository.save(event);
        return usageMapper.toResponse(event);
    }

    @Transactional(readOnly = true)
    public PagedResponse<UsageEventResponse> listBySubscription(UUID subscriptionId, Pageable pageable) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Page<UsageEvent> page = usageRepository.findAllByOrgIdAndSubscriptionId(orgId, subscriptionId, pageable);
        return PagedResponse.of(usageMapper.toResponseList(page.getContent()), page);
    }

    @Transactional(readOnly = true)
    public PagedResponse<UsageEventResponse> listByCustomer(UUID customerId, Pageable pageable) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Page<UsageEvent> page = usageRepository.findAllByOrgIdAndCustomerId(orgId, customerId, pageable);
        return PagedResponse.of(usageMapper.toResponseList(page.getContent()), page);
    }
}
