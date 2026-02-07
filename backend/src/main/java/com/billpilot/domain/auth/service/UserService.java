package com.billpilot.domain.auth.service;

import com.billpilot.common.dto.PagedResponse;
import com.billpilot.common.exception.ConflictException;
import com.billpilot.common.exception.ResourceNotFoundException;
import com.billpilot.domain.auth.dto.*;
import com.billpilot.domain.auth.entity.User;
import com.billpilot.domain.auth.mapper.UserMapper;
import com.billpilot.domain.auth.repository.UserRepository;
import com.billpilot.security.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public PagedResponse<UserResponse> listUsers(Pageable pageable) {
        UUID orgId = TenantContext.getCurrentOrgId();
        Page<User> page = userRepository.findAllByOrgId(orgId, pageable);
        return PagedResponse.of(userMapper.toResponseList(page.getContent()), page);
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(UUID userId) {
        UUID orgId = TenantContext.getCurrentOrgId();
        User user = userRepository.findByIdAndOrgId(userId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse inviteUser(InviteUserRequest request) {
        UUID orgId = TenantContext.getCurrentOrgId();
        if (userRepository.existsByEmailAndOrgId(request.getEmail(), orgId)) {
            throw new ConflictException("User with this email already exists in this organization");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getRole())
                .build();
        user.setOrgId(orgId);
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateUser(UUID userId, UpdateUserRequest request) {
        UUID orgId = TenantContext.getCurrentOrgId();
        User user = userRepository.findByIdAndOrgId(userId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getRole() != null) user.setRole(request.getRole());
        if (request.getActive() != null) user.setActive(request.getActive());

        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Transactional
    public void deleteUser(UUID userId) {
        UUID orgId = TenantContext.getCurrentOrgId();
        User user = userRepository.findByIdAndOrgId(userId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        user.setActive(false);
        userRepository.save(user);
    }
}
