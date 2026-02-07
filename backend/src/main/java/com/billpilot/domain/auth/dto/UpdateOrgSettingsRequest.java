package com.billpilot.domain.auth.dto;

import lombok.Data;

@Data
public class UpdateOrgSettingsRequest {
    private String invoicePrefix;
    private String defaultCurrency;
    private Integer trialDays;
    private Integer paymentTermsDays;
    private String webhookUrl;
    private String webhookSecret;
}
