package com.billpilot.domain.invoice.service;

import com.billpilot.domain.auth.entity.OrgSettings;
import com.billpilot.domain.auth.repository.OrgSettingsRepository;
import com.billpilot.domain.invoice.entity.InvoiceNumberSequence;
import com.billpilot.domain.invoice.repository.InvoiceNumberSequenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceNumberServiceTest {

    @Mock
    private InvoiceNumberSequenceRepository sequenceRepository;

    @Mock
    private OrgSettingsRepository orgSettingsRepository;

    @InjectMocks
    private InvoiceNumberService invoiceNumberService;

    @Test
    void generateInvoiceNumber_shouldGenerateCorrectFormat() {
        UUID orgId = UUID.randomUUID();
        int year = Year.now().getValue();

        InvoiceNumberSequence seq = InvoiceNumberSequence.builder()
                .orgId(orgId).year(year).lastNumber(5).build();

        when(sequenceRepository.findByOrgIdAndYearForUpdate(orgId, year)).thenReturn(Optional.of(seq));
        when(sequenceRepository.save(any())).thenReturn(seq);

        OrgSettings settings = OrgSettings.builder().invoicePrefix("TEST").build();
        when(orgSettingsRepository.findByOrgId(orgId)).thenReturn(Optional.of(settings));

        String number = invoiceNumberService.generateInvoiceNumber(orgId);

        assertEquals("TEST-" + year + "-00006", number);
        assertEquals(6, seq.getLastNumber());
    }

    @Test
    void generateInvoiceNumber_shouldCreateNewSequenceIfNotExists() {
        UUID orgId = UUID.randomUUID();
        int year = Year.now().getValue();

        when(sequenceRepository.findByOrgIdAndYearForUpdate(orgId, year)).thenReturn(Optional.empty());
        when(sequenceRepository.save(any())).thenAnswer(inv -> {
            InvoiceNumberSequence s = inv.getArgument(0);
            s.setLastNumber(s.getLastNumber());
            return s;
        });
        when(orgSettingsRepository.findByOrgId(orgId)).thenReturn(Optional.empty());

        String number = invoiceNumberService.generateInvoiceNumber(orgId);

        assertTrue(number.startsWith("INV-" + year + "-"));
    }
}
