package com.billpilot.domain.webhook.mapper;

import com.billpilot.domain.webhook.dto.WebhookEventResponse;
import com.billpilot.domain.webhook.entity.WebhookEvent;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WebhookMapper {
    WebhookEventResponse toResponse(WebhookEvent event);
    List<WebhookEventResponse> toResponseList(List<WebhookEvent> events);
}
