package com.billpilot.domain.usage.mapper;

import com.billpilot.domain.usage.dto.UsageEventResponse;
import com.billpilot.domain.usage.entity.UsageEvent;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsageMapper {
    UsageEventResponse toResponse(UsageEvent event);
    List<UsageEventResponse> toResponseList(List<UsageEvent> events);
}
