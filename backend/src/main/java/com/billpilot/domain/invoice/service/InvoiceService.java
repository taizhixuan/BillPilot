package com.billpilot.domain.invoice.service;

import com.billpilot.common.dto.PagedResponse;
import com.billpilot.common.exception.BadRequestException;
import com.billpilot.common.exception.ResourceNotFoundException;
import com.billpilot.domain.audit.service.AuditService;
import com.billpilot.domain.invoice.dto.InvoiceResponse;
import com.billpilot.domain.invoice.entity.Invoice;
import com.billpilot.domain.invoice.entity.InvoiceLineItem;
import com.billpilot.domain.invoice.entity.InvoiceStatus;
import com.billpilot.domain.invoice.mapper.InvoiceMapper;
import com.billpilot.domain.invoice.repository.InvoiceRepository;
import com.billpilot.domain.plan.entity.Plan;
import com.billpilot.domain.subscription.entity.Subscription;
import com.billpilot.security.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final InvoiceNumberService invoiceNumberService;
    private final AuditService auditService;

    @Transactional(readOnly = true)
    public PagedResponse<InvoiceResponse> listInvoices(Pageable pageable) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Page<Invoice> page = invoiceRepository.findAllByOrgId(orgId, pageable);
        return PagedResponse.of(invoiceMapper.toResponseList(page.getContent()), page);
    }

    @Transactional(readOnly = true)
    public PagedResponse<InvoiceResponse> listByCustomer(UUID customerId, Pageable pageable) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Page<Invoice> page = invoiceRepository.findAllByOrgIdAndCustomerId(orgId, customerId, pageable);
        return PagedResponse.of(invoiceMapper.toResponseList(page.getContent()), page);
    }

    @Transactional(readOnly = true)
    public InvoiceResponse getInvoice(UUID invoiceId) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Invoice invoice = invoiceRepository.findByIdAndOrgId(invoiceId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", invoiceId));
        return invoiceMapper.toResponse(invoice);
    }

    @Transactional
    public Invoice generateInvoice(Subscription subscription, Plan plan) {
        BigDecimal amount = plan.getPrice().multiply(BigDecimal.valueOf(subscription.getQuantity()));

        Invoice invoice = Invoice.builder()
                .customerId(subscription.getCustomerId())
                .subscriptionId(subscription.getId())
                .invoiceNumber(invoiceNumberService.generateInvoiceNumber(subscription.getOrgId()))
                .status(InvoiceStatus.OPEN)
                .subtotal(amount)
                .tax(BigDecimal.ZERO)
                .total(amount)
                .dueDate(LocalDate.now().plusDays(30))
                .build();
        invoice.setOrgId(subscription.getOrgId());

        InvoiceLineItem lineItem = InvoiceLineItem.builder()
                .description(plan.getName() + " - " + plan.getBillingInterval() + " (x" + subscription.getQuantity() + ")")
                .quantity(subscription.getQuantity())
                .unitPrice(plan.getPrice())
                .amount(amount)
                .build();
        invoice.addLineItem(lineItem);

        return invoiceRepository.save(invoice);
    }

    @Transactional
    public Invoice generateProrationInvoice(Subscription subscription, Plan plan, BigDecimal amount, String description) {
        Invoice invoice = Invoice.builder()
                .customerId(subscription.getCustomerId())
                .subscriptionId(subscription.getId())
                .invoiceNumber(invoiceNumberService.generateInvoiceNumber(subscription.getOrgId()))
                .status(InvoiceStatus.OPEN)
                .subtotal(amount)
                .tax(BigDecimal.ZERO)
                .total(amount)
                .dueDate(LocalDate.now().plusDays(30))
                .build();
        invoice.setOrgId(subscription.getOrgId());

        InvoiceLineItem lineItem = InvoiceLineItem.builder()
                .description(description + " - " + plan.getName())
                .quantity(1)
                .unitPrice(amount)
                .amount(amount)
                .build();
        invoice.addLineItem(lineItem);

        return invoiceRepository.save(invoice);
    }

    @Transactional
    public InvoiceResponse markPaid(UUID invoiceId) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Invoice invoice = invoiceRepository.findByIdAndOrgId(invoiceId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", invoiceId));

        if (invoice.getStatus() != InvoiceStatus.OPEN && invoice.getStatus() != InvoiceStatus.PAST_DUE) {
            throw new BadRequestException("Invoice cannot be marked as paid in status: " + invoice.getStatus());
        }

        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setPaidAt(Instant.now());
        invoice = invoiceRepository.save(invoice);

        auditService.logCurrentUser("MARK_PAID", "Invoice", invoice.getId(), "Marked invoice " + invoice.getInvoiceNumber() + " as paid");
        return invoiceMapper.toResponse(invoice);
    }

    @Transactional
    public InvoiceResponse voidInvoice(UUID invoiceId) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Invoice invoice = invoiceRepository.findByIdAndOrgId(invoiceId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", invoiceId));

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new BadRequestException("Cannot void a paid invoice");
        }

        invoice.setStatus(InvoiceStatus.VOID);
        invoice = invoiceRepository.save(invoice);

        auditService.logCurrentUser("VOID", "Invoice", invoice.getId(), "Voided invoice " + invoice.getInvoiceNumber());
        return invoiceMapper.toResponse(invoice);
    }
}
