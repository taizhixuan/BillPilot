package com.billpilot.domain.webhook.controller;

import com.billpilot.common.dto.PagedResponse;
import com.billpilot.domain.webhook.dto.WebhookEventResponse;
import com.billpilot.domain.webhook.service.WebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;

    @GetMapping
    public ResponseEntity<PagedResponse<WebhookEventResponse>> listEvents(
            @PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(webhookService.listEvents(pageable));
    }
}
