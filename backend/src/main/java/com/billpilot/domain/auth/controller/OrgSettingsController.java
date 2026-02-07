package com.billpilot.domain.auth.controller;

import com.billpilot.domain.auth.dto.OrgSettingsResponse;
import com.billpilot.domain.auth.dto.UpdateOrgSettingsRequest;
import com.billpilot.domain.auth.service.OrgSettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/settings")
@RequiredArgsConstructor
public class OrgSettingsController {

    private final OrgSettingsService orgSettingsService;

    @GetMapping
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    public ResponseEntity<OrgSettingsResponse> getSettings() {
        return ResponseEntity.ok(orgSettingsService.getSettings());
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
    public ResponseEntity<OrgSettingsResponse> updateSettings(@Valid @RequestBody UpdateOrgSettingsRequest request) {
        return ResponseEntity.ok(orgSettingsService.updateSettings(request));
    }
}
