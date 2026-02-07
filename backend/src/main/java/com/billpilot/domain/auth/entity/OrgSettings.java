package com.billpilot.domain.auth.entity;

import com.billpilot.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "org_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrgSettings extends BaseEntity {

    @Column(name = "org_id", nullable = false, unique = true)
    private UUID orgId;

    @Builder.Default
    @Column(name = "invoice_prefix", nullable = false, length = 10)
    private String invoicePrefix = "INV";

    @Builder.Default
    @Column(name = "default_currency", nullable = false, length = 3)
    private String defaultCurrency = "USD";

    @Builder.Default
    @Column(name = "trial_days", nullable = false)
    private int trialDays = 14;

    @Builder.Default
    @Column(name = "payment_terms_days", nullable = false)
    private int paymentTermsDays = 30;

    @Column(name = "webhook_url")
    private String webhookUrl;

    @Column(name = "webhook_secret")
    private String webhookSecret;
}
