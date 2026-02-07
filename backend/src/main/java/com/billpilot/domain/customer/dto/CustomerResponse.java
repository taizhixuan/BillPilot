package com.billpilot.domain.customer.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class CustomerResponse {
    private UUID id;
    private UUID orgId;
    private String name;
    private String email;
    private String company;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String notes;
    private boolean active;
    private Instant createdAt;
}
