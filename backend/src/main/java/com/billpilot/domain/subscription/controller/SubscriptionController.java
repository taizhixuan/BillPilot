package com.billpilot.domain.subscription.controller;

import com.billpilot.common.dto.PagedResponse;
import com.billpilot.domain.subscription.dto.*;
import com.billpilot.domain.subscription.service.SubscriptionService;
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
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('OWNER','ADMIN','BILLING_MGR')")
    public ResponseEntity<PagedResponse<SubscriptionResponse>> listSubscriptions(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(subscriptionService.listSubscriptions(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN','BILLING_MGR')")
    public ResponseEntity<SubscriptionResponse> getSubscription(@PathVariable UUID id) {
        return ResponseEntity.ok(subscriptionService.getSubscription(id));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN','BILLING_MGR')")
    public ResponseEntity<PagedResponse<SubscriptionResponse>> listByCustomer(
            @PathVariable UUID customerId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(subscriptionService.listByCustomer(customerId, pageable));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER','ADMIN','BILLING_MGR')")
    public ResponseEntity<SubscriptionResponse> createSubscription(@Valid @RequestBody CreateSubscriptionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.createSubscription(request));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN','BILLING_MGR')")
    public ResponseEntity<SubscriptionResponse> cancelSubscription(@PathVariable UUID id) {
        return ResponseEntity.ok(subscriptionService.cancelSubscription(id));
    }

    @PostMapping("/{id}/change-plan")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN','BILLING_MGR')")
    public ResponseEntity<SubscriptionResponse> changePlan(@PathVariable UUID id, @Valid @RequestBody ChangePlanRequest request) {
        return ResponseEntity.ok(subscriptionService.changePlan(id, request));
    }
}
