package com.billpilot.domain.plan.mapper;

import com.billpilot.domain.plan.dto.PlanResponse;
import com.billpilot.domain.plan.entity.Plan;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlanMapper {
    PlanResponse toResponse(Plan plan);
    List<PlanResponse> toResponseList(List<Plan> plans);
}
