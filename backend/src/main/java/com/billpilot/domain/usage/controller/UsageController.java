package com.billpilot.domain.usage.controller;

import com.billpilot.common.dto.PagedResponse;
import com.billpilot.domain.usage.dto.*;
import com.billpilot.domain.usage.service.UsageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usage")
@RequiredArgsConstructor
public class UsageController {

    private final UsageService usageService;

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER','ADMIN','BILLING_MGR')")
    public ResponseEntity<UsageEventResponse> recordUsage(@Valid @RequestBody RecordUsageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usageService.recordUsage(request));
    }

    @GetMapping("/subscription/{subscriptionId}")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN','BILLING_MGR')")
    public ResponseEntity<PagedResponse<UsageEventResponse>> listBySubscription(
            @PathVariable UUID subscriptionId, @PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(usageService.listBySubscription(subscriptionId, pageable));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN','BILLING_MGR')")
    public ResponseEntity<PagedResponse<UsageEventResponse>> listByCustomer(
            @PathVariable UUID customerId, @PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(usageService.listByCustomer(customerId, pageable));
    }
}
