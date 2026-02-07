package com.billpilot.domain.subscription.mapper;

import com.billpilot.domain.subscription.dto.SubscriptionResponse;
import com.billpilot.domain.subscription.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(source = "plan.name", target = "planName")
    SubscriptionResponse toResponse(Subscription subscription);

    List<SubscriptionResponse> toResponseList(List<Subscription> subscriptions);
}
