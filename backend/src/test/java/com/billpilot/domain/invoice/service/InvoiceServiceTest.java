package com.billpilot.domain.invoice.service;

import com.billpilot.common.exception.BadRequestException;
import com.billpilot.domain.audit.service.AuditService;
import com.billpilot.domain.invoice.entity.Invoice;
import com.billpilot.domain.invoice.entity.InvoiceStatus;
import com.billpilot.domain.invoice.mapper.InvoiceMapper;
import com.billpilot.domain.invoice.repository.InvoiceRepository;
import com.billpilot.domain.plan.entity.BillingInterval;
import com.billpilot.domain.plan.entity.Plan;
import com.billpilot.domain.subscription.entity.Subscription;
import com.billpilot.security.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock private InvoiceRepository invoiceRepository;
    @Mock private InvoiceMapper invoiceMapper;
    @Mock private InvoiceNumberService invoiceNumberService;
    @Mock private AuditService auditService;
    @InjectMocks private InvoiceService invoiceService;

    private UUID orgId;

    @BeforeEach
    void setUp() {
        orgId = UUID.randomUUID();
        TenantContext.setCurrentOrgId(orgId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void generateInvoice_shouldCreateInvoiceWithCorrectTotal() {
        Subscription sub = Subscription.builder()
                .customerId(UUID.randomUUID())
                .quantity(3)
                .build();
        sub.setOrgId(orgId);
        sub.setId(UUID.randomUUID());

        Plan plan = Plan.builder()
                .name("Pro")
                .price(BigDecimal.valueOf(99))
                .billingInterval(BillingInterval.MONTHLY)
                .build();

        when(invoiceNumberService.generateInvoiceNumber(orgId)).thenReturn("TEST-2026-00001");
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(inv -> inv.getArgument(0));

        Invoice result = invoiceService.generateInvoice(sub, plan);

        assertEquals(BigDecimal.valueOf(297), result.getSubtotal());
        assertEquals(BigDecimal.valueOf(297), result.getTotal());
        assertEquals(InvoiceStatus.OPEN, result.getStatus());
        assertEquals("TEST-2026-00001", result.getInvoiceNumber());
    }

    @Test
    void markPaid_shouldThrowForAlreadyPaidInvoice() {
        Invoice invoice = Invoice.builder()
                .status(InvoiceStatus.PAID)
                .build();
        invoice.setId(UUID.randomUUID());

        when(invoiceRepository.findByIdAndOrgId(invoice.getId(), orgId)).thenReturn(Optional.of(invoice));

        assertThrows(BadRequestException.class, () -> invoiceService.markPaid(invoice.getId()));
    }
}
