package com.billpilot.domain.payment.service;

import com.billpilot.common.dto.PagedResponse;
import com.billpilot.common.exception.BadRequestException;
import com.billpilot.common.exception.ResourceNotFoundException;
import com.billpilot.domain.audit.service.AuditService;
import com.billpilot.domain.invoice.entity.Invoice;
import com.billpilot.domain.invoice.entity.InvoiceStatus;
import com.billpilot.domain.invoice.repository.InvoiceRepository;
import com.billpilot.domain.payment.dto.*;
import com.billpilot.domain.payment.entity.Payment;
import com.billpilot.domain.payment.entity.PaymentStatus;
import com.billpilot.domain.payment.mapper.PaymentMapper;
import com.billpilot.domain.payment.repository.PaymentRepository;
import com.billpilot.security.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentMapper paymentMapper;
    private final AuditService auditService;

    @Transactional(readOnly = true)
    public PagedResponse<PaymentResponse> listPayments(Pageable pageable) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Page<Payment> page = paymentRepository.findAllByOrgId(orgId, pageable);
        return PagedResponse.of(paymentMapper.toResponseList(page.getContent()), page);
    }

    @Transactional(readOnly = true)
    public PagedResponse<PaymentResponse> listByCustomer(UUID customerId, Pageable pageable) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Page<Payment> page = paymentRepository.findAllByOrgIdAndCustomerId(orgId, customerId, pageable);
        return PagedResponse.of(paymentMapper.toResponseList(page.getContent()), page);
    }

    @Transactional
    public PaymentResponse processPayment(CreatePaymentRequest request) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Invoice invoice = invoiceRepository.findByIdAndOrgId(request.getInvoiceId(), orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", request.getInvoiceId()));

        if (invoice.getStatus() != InvoiceStatus.OPEN && invoice.getStatus() != InvoiceStatus.PAST_DUE) {
            throw new BadRequestException("Invoice is not payable in status: " + invoice.getStatus());
        }

        // Simulate payment processing: 90% success, 10% failure
        boolean success = Math.random() < 0.9;

        Payment payment = Payment.builder()
                .invoiceId(invoice.getId())
                .customerId(invoice.getCustomerId())
                .amount(invoice.getTotal())
                .paymentMethod(request.getPaymentMethod())
                .transactionId("txn_" + UUID.randomUUID().toString().substring(0, 12))
                .status(success ? PaymentStatus.SUCCEEDED : PaymentStatus.FAILED)
                .failureReason(success ? null : "Card declined")
                .build();
        payment.setOrgId(orgId);
        payment = paymentRepository.save(payment);

        if (success) {
            invoice.setStatus(InvoiceStatus.PAID);
            invoice.setPaidAt(Instant.now());
            invoiceRepository.save(invoice);
        }

        auditService.logCurrentUser(
                success ? "PAYMENT_SUCCESS" : "PAYMENT_FAILED",
                "Payment", payment.getId(),
                "Payment for invoice " + invoice.getInvoiceNumber() + ": " + (success ? "succeeded" : "failed")
        );

        payment.setInvoice(invoice);
        payment.setCustomer(invoice.getCustomer());
        return paymentMapper.toResponse(payment);
    }
}
