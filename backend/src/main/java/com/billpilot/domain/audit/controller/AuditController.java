package com.billpilot.domain.audit.controller;

import com.billpilot.common.dto.PagedResponse;
import com.billpilot.domain.audit.dto.AuditLogResponse;
import com.billpilot.domain.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    public ResponseEntity<PagedResponse<AuditLogResponse>> getAuditLogs(
            @PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(auditService.getAuditLogs(pageable));
    }

    @GetMapping("/entity/{entityId}")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    public ResponseEntity<PagedResponse<AuditLogResponse>> getAuditLogsByEntity(
            @PathVariable UUID entityId,
            @PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(auditService.getAuditLogsByEntity(entityId, pageable));
    }
}
