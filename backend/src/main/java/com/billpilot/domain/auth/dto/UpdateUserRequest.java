package com.billpilot.domain.auth.dto;

import com.billpilot.domain.auth.entity.Role;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private Role role;
    private Boolean active;
}
