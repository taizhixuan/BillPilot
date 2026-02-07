package com.billpilot.domain.customer.entity;

import com.billpilot.common.TenantAwareEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends TenantAwareEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String company;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String country;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(columnDefinition = "text")
    private String notes;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;
}
