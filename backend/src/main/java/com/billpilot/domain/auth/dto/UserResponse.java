package com.billpilot.domain.auth.dto;

import com.billpilot.domain.auth.entity.Role;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class UserResponse {
    private UUID id;
    private UUID orgId;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private boolean active;
    private Instant createdAt;
}
