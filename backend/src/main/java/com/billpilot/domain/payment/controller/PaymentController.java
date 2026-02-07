package com.billpilot.domain.payment.controller;

import com.billpilot.common.dto.PagedResponse;
import com.billpilot.domain.payment.dto.*;
import com.billpilot.domain.payment.service.PaymentService;
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
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('OWNER','ADMIN','BILLING_MGR')")
    public ResponseEntity<PagedResponse<PaymentResponse>> listPayments(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(paymentService.listPayments(pageable));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN','BILLING_MGR')")
    public ResponseEntity<PagedResponse<PaymentResponse>> listByCustomer(
            @PathVariable UUID customerId, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(paymentService.listByCustomer(customerId, pageable));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER','ADMIN','BILLING_MGR')")
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody CreatePaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.processPayment(request));
    }
}
