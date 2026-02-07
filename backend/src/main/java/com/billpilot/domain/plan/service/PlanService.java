package com.billpilot.domain.plan.service;

import com.billpilot.common.dto.PagedResponse;
import com.billpilot.common.exception.ConflictException;
import com.billpilot.common.exception.ResourceNotFoundException;
import com.billpilot.domain.audit.service.AuditService;
import com.billpilot.domain.plan.dto.*;
import com.billpilot.domain.plan.entity.Plan;
import com.billpilot.domain.plan.mapper.PlanMapper;
import com.billpilot.domain.plan.repository.PlanRepository;
import com.billpilot.security.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final PlanMapper planMapper;
    private final AuditService auditService;

    @Transactional(readOnly = true)
    public PagedResponse<PlanResponse> listPlans(Pageable pageable) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Page<Plan> page = planRepository.findAllByOrgId(orgId, pageable);
        return PagedResponse.of(planMapper.toResponseList(page.getContent()), page);
    }

    @Transactional(readOnly = true)
    public PlanResponse getPlan(UUID planId) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Plan plan = planRepository.findByIdAndOrgId(planId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", planId));
        return planMapper.toResponse(plan);
    }

    @Transactional
    public PlanResponse createPlan(CreatePlanRequest request) {
        UUID orgId = TenantContext.getCurrentOrgId();
        if (planRepository.existsByNameAndOrgId(request.getName(), orgId)) {
            throw new ConflictException("Plan with name '" + request.getName() + "' already exists");
        }

        Plan plan = Plan.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .billingInterval(request.getBillingInterval())
                .trialDays(request.getTrialDays())
                .features(request.getFeatures())
                .maxSeats(request.getMaxSeats())
                .usageLimit(request.getUsageLimit())
                .build();
        plan.setOrgId(orgId);
        plan = planRepository.save(plan);

        auditService.logCurrentUser("CREATE", "Plan", plan.getId(), "Created plan: " + plan.getName());
        return planMapper.toResponse(plan);
    }

    @Transactional
    public PlanResponse updatePlan(UUID planId, UpdatePlanRequest request) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Plan plan = planRepository.findByIdAndOrgId(planId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", planId));

        if (request.getName() != null) plan.setName(request.getName());
        if (request.getDescription() != null) plan.setDescription(request.getDescription());
        if (request.getPrice() != null) plan.setPrice(request.getPrice());
        if (request.getBillingInterval() != null) plan.setBillingInterval(request.getBillingInterval());
        if (request.getTrialDays() != null) plan.setTrialDays(request.getTrialDays());
        if (request.getFeatures() != null) plan.setFeatures(request.getFeatures());
        if (request.getActive() != null) plan.setActive(request.getActive());
        if (request.getMaxSeats() != null) plan.setMaxSeats(request.getMaxSeats());
        if (request.getUsageLimit() != null) plan.setUsageLimit(request.getUsageLimit());

        plan = planRepository.save(plan);
        auditService.logCurrentUser("UPDATE", "Plan", plan.getId(), "Updated plan: " + plan.getName());
        return planMapper.toResponse(plan);
    }

    @Transactional
    public void deletePlan(UUID planId) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Plan plan = planRepository.findByIdAndOrgId(planId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", planId));
        plan.setActive(false);
        planRepository.save(plan);
        auditService.logCurrentUser("DELETE", "Plan", plan.getId(), "Deactivated plan: " + plan.getName());
    }
}
