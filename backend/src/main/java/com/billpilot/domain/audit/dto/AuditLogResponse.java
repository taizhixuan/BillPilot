package com.billpilot.domain.audit.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class AuditLogResponse {
    private UUID id;
    private UUID orgId;
    private UUID userId;
    private String userEmail;
    private String action;
    private String entityType;
    private UUID entityId;
    private String details;
    private Instant createdAt;
}
