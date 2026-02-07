package com.billpilot.domain.customer.mapper;

import com.billpilot.domain.customer.dto.CustomerResponse;
import com.billpilot.domain.customer.entity.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerResponse toResponse(Customer customer);
    List<CustomerResponse> toResponseList(List<Customer> customers);
}
