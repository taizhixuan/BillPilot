package com.billpilot.domain.auth.mapper;

import com.billpilot.domain.auth.dto.OrgSettingsResponse;
import com.billpilot.domain.auth.entity.OrgSettings;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrgSettingsMapper {
    OrgSettingsResponse toResponse(OrgSettings settings);
}
