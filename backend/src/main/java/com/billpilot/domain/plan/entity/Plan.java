package com.billpilot.domain.plan.entity;

import com.billpilot.common.TenantAwareEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan extends TenantAwareEntity {

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_interval", nullable = false, length = 20)
    private BillingInterval billingInterval;

    @Builder.Default
    @Column(name = "trial_days", nullable = false)
    private int trialDays = 0;

    @Column(columnDefinition = "text")
    private String features;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "max_seats")
    private Integer maxSeats;

    @Column(name = "usage_limit")
    private Long usageLimit;
}
