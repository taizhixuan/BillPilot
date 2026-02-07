package com.billpilot.domain.audit.service;

import com.billpilot.common.dto.PagedResponse;
import com.billpilot.domain.audit.dto.AuditLogResponse;
import com.billpilot.domain.audit.entity.AuditLog;
import com.billpilot.domain.audit.mapper.AuditLogMapper;
import com.billpilot.domain.audit.repository.AuditLogRepository;
import com.billpilot.security.TenantContext;
import com.billpilot.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository repository;
    private final AuditLogMapper mapper;

    @Async
    public void log(UUID orgId, UUID userId, String userEmail, String action,
                    String entityType, UUID entityId, String details) {
        AuditLog entry = AuditLog.builder()
                .orgId(orgId)
                .userId(userId)
                .userEmail(userEmail)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .details(details)
                .build();
        repository.save(entry);
    }

    public void logCurrentUser(String action, String entityType, UUID entityId, String details) {
        UUID orgId = TenantContext.getCurrentOrgId();
        var auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = null;
        String email = null;
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal principal) {
            userId = principal.getId();
            email = principal.getEmail();
        }
        log(orgId, userId, email, action, entityType, entityId, details);
    }

    @Transactional(readOnly = true)
    public PagedResponse<AuditLogResponse> getAuditLogs(Pageable pageable) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Page<AuditLog> page = repository.findAllByOrgIdOrderByCreatedAtDesc(orgId, pageable);
        return PagedResponse.of(mapper.toResponseList(page.getContent()), page);
    }

    @Transactional(readOnly = true)
    public PagedResponse<AuditLogResponse> getAuditLogsByEntity(UUID entityId, Pageable pageable) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Page<AuditLog> page = repository.findAllByOrgIdAndEntityIdOrderByCreatedAtDesc(orgId, entityId, pageable);
        return PagedResponse.of(mapper.toResponseList(page.getContent()), page);
    }
}
