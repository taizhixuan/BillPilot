package com.billpilot.domain.customer.service;

import com.billpilot.common.dto.PagedResponse;
import com.billpilot.common.exception.ConflictException;
import com.billpilot.common.exception.ResourceNotFoundException;
import com.billpilot.domain.audit.service.AuditService;
import com.billpilot.domain.customer.dto.*;
import com.billpilot.domain.customer.entity.Customer;
import com.billpilot.domain.customer.mapper.CustomerMapper;
import com.billpilot.domain.customer.repository.CustomerRepository;
import com.billpilot.security.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AuditService auditService;

    @Transactional(readOnly = true)
    public PagedResponse<CustomerResponse> listCustomers(String search, Pageable pageable) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Page<Customer> page;
        if (search != null && !search.isBlank()) {
            page = customerRepository.searchByOrgId(orgId, search.trim(), pageable);
        } else {
            page = customerRepository.findAllByOrgId(orgId, pageable);
        }
        return PagedResponse.of(customerMapper.toResponseList(page.getContent()), page);
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomer(UUID customerId) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Customer customer = customerRepository.findByIdAndOrgId(customerId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", customerId));
        return customerMapper.toResponse(customer);
    }

    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        UUID orgId = TenantContext.getCurrentOrgId();
        if (customerRepository.existsByEmailAndOrgId(request.getEmail(), orgId)) {
            throw new ConflictException("Customer with email '" + request.getEmail() + "' already exists");
        }

        Customer customer = Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .company(request.getCompany())
                .phone(request.getPhone())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .postalCode(request.getPostalCode())
                .notes(request.getNotes())
                .build();
        customer.setOrgId(orgId);
        customer = customerRepository.save(customer);

        auditService.logCurrentUser("CREATE", "Customer", customer.getId(), "Created customer: " + customer.getName());
        return customerMapper.toResponse(customer);
    }

    @Transactional
    public CustomerResponse updateCustomer(UUID customerId, UpdateCustomerRequest request) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Customer customer = customerRepository.findByIdAndOrgId(customerId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", customerId));

        if (request.getName() != null) customer.setName(request.getName());
        if (request.getEmail() != null) customer.setEmail(request.getEmail());
        if (request.getCompany() != null) customer.setCompany(request.getCompany());
        if (request.getPhone() != null) customer.setPhone(request.getPhone());
        if (request.getAddress() != null) customer.setAddress(request.getAddress());
        if (request.getCity() != null) customer.setCity(request.getCity());
        if (request.getState() != null) customer.setState(request.getState());
        if (request.getCountry() != null) customer.setCountry(request.getCountry());
        if (request.getPostalCode() != null) customer.setPostalCode(request.getPostalCode());
        if (request.getNotes() != null) customer.setNotes(request.getNotes());
        if (request.getActive() != null) customer.setActive(request.getActive());

        customer = customerRepository.save(customer);
        auditService.logCurrentUser("UPDATE", "Customer", customer.getId(), "Updated customer: " + customer.getName());
        return customerMapper.toResponse(customer);
    }

    @Transactional
    public void deleteCustomer(UUID customerId) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Customer customer = customerRepository.findByIdAndOrgId(customerId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", customerId));
        customer.setActive(false);
        customerRepository.save(customer);
        auditService.logCurrentUser("DELETE", "Customer", customer.getId(), "Deactivated customer: " + customer.getName());
    }
}
