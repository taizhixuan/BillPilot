package com.billpilot.domain.auth.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class OrgSettingsResponse {
    private UUID id;
    private UUID orgId;
    private String invoicePrefix;
    private String defaultCurrency;
    private int trialDays;
    private int paymentTermsDays;
    private String webhookUrl;
}
