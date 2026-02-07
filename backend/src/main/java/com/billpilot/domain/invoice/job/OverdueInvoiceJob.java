package com.billpilot.domain.invoice.job;

import com.billpilot.domain.invoice.entity.Invoice;
import com.billpilot.domain.invoice.entity.InvoiceStatus;
import com.billpilot.domain.invoice.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OverdueInvoiceJob {

    private final InvoiceRepository invoiceRepository;

    @Scheduled(cron = "0 30 2 * * *")
    @Transactional
    public void markOverdueInvoices() {
        List<Invoice> overdue = invoiceRepository.findOverdueInvoices(InvoiceStatus.OPEN, LocalDate.now());
        log.info("Found {} overdue invoices", overdue.size());

        for (Invoice invoice : overdue) {
            invoice.setStatus(InvoiceStatus.PAST_DUE);
            invoiceRepository.save(invoice);
            log.info("Marked invoice {} as past due", invoice.getInvoiceNumber());
        }
    }
}
