package com.billpilot.domain.invoice.controller;

import com.billpilot.common.dto.PagedResponse;
import com.billpilot.domain.invoice.dto.InvoiceResponse;
import com.billpilot.domain.invoice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<PagedResponse<InvoiceResponse>> listInvoices(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(invoiceService.listInvoices(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoice(@PathVariable UUID id) {
        return ResponseEntity.ok(invoiceService.getInvoice(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<PagedResponse<InvoiceResponse>> listByCustomer(
            @PathVariable UUID customerId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(invoiceService.listByCustomer(customerId, pageable));
    }

    @PostMapping("/{id}/mark-paid")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN','BILLING_MGR')")
    public ResponseEntity<InvoiceResponse> markPaid(@PathVariable UUID id) {
        return ResponseEntity.ok(invoiceService.markPaid(id));
    }

    @PostMapping("/{id}/void")
    @PreAuthorize("hasAnyRole('OWNER','ADMIN','BILLING_MGR')")
    public ResponseEntity<InvoiceResponse> voidInvoice(@PathVariable UUID id) {
        return ResponseEntity.ok(invoiceService.voidInvoice(id));
    }
}
