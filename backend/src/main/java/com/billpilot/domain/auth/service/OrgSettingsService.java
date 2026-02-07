package com.billpilot.domain.auth.service;

import com.billpilot.common.exception.ResourceNotFoundException;
import com.billpilot.domain.auth.dto.OrgSettingsResponse;
import com.billpilot.domain.auth.dto.UpdateOrgSettingsRequest;
import com.billpilot.domain.auth.entity.OrgSettings;
import com.billpilot.domain.auth.mapper.OrgSettingsMapper;
import com.billpilot.domain.auth.repository.OrgSettingsRepository;
import com.billpilot.security.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrgSettingsService {

    private final OrgSettingsRepository repository;
    private final OrgSettingsMapper mapper;

    @Transactional(readOnly = true)
    public OrgSettingsResponse getSettings() {
        UUID orgId = TenantContext.getCurrentOrgId();
        OrgSettings settings = repository.findByOrgId(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("OrgSettings", orgId));
        return mapper.toResponse(settings);
    }

    @Transactional
    public OrgSettingsResponse updateSettings(UpdateOrgSettingsRequest request) {
        UUID orgId = TenantContext.getCurrentOrgId();
        OrgSettings settings = repository.findByOrgId(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("OrgSettings", orgId));

        if (request.getInvoicePrefix() != null) settings.setInvoicePrefix(request.getInvoicePrefix());
        if (request.getDefaultCurrency() != null) settings.setDefaultCurrency(request.getDefaultCurrency());
        if (request.getTrialDays() != null) settings.setTrialDays(request.getTrialDays());
        if (request.getPaymentTermsDays() != null) settings.setPaymentTermsDays(request.getPaymentTermsDays());
        if (request.getWebhookUrl() != null) settings.setWebhookUrl(request.getWebhookUrl());
        if (request.getWebhookSecret() != null) settings.setWebhookSecret(request.getWebhookSecret());

        settings = repository.save(settings);
        return mapper.toResponse(settings);
    }
}
