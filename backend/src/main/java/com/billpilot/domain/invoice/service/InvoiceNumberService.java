package com.billpilot.domain.invoice.service;

import com.billpilot.domain.auth.entity.OrgSettings;
import com.billpilot.domain.auth.repository.OrgSettingsRepository;
import com.billpilot.domain.invoice.entity.InvoiceNumberSequence;
import com.billpilot.domain.invoice.repository.InvoiceNumberSequenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceNumberService {

    private final InvoiceNumberSequenceRepository sequenceRepository;
    private final OrgSettingsRepository orgSettingsRepository;

    @Transactional
    public String generateInvoiceNumber(UUID orgId) {
        int currentYear = Year.now().getValue();

        InvoiceNumberSequence seq = sequenceRepository.findByOrgIdAndYearForUpdate(orgId, currentYear)
                .orElseGet(() -> {
                    InvoiceNumberSequence newSeq = InvoiceNumberSequence.builder()
                            .orgId(orgId)
                            .year(currentYear)
                            .lastNumber(0)
                            .build();
                    return sequenceRepository.save(newSeq);
                });

        seq.setLastNumber(seq.getLastNumber() + 1);
        sequenceRepository.save(seq);

        String prefix = orgSettingsRepository.findByOrgId(orgId)
                .map(OrgSettings::getInvoicePrefix)
                .orElse("INV");

        return String.format("%s-%d-%05d", prefix, currentYear, seq.getLastNumber());
    }
}
