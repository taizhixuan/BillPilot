package com.billpilot.domain.customer.dto;

import lombok.Data;

@Data
public class UpdateCustomerRequest {
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
    private Boolean active;
}
