package com.billpilot.domain.auth.service;

import com.billpilot.common.exception.BadRequestException;
import com.billpilot.common.exception.ConflictException;
import com.billpilot.domain.auth.dto.*;
import com.billpilot.domain.auth.entity.*;
import com.billpilot.domain.auth.mapper.UserMapper;
import com.billpilot.domain.auth.repository.*;
import com.billpilot.security.JwtTokenProvider;
import com.billpilot.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OrganizationRepository orgRepository;
    private final UserRepository userRepository;
    private final OrgSettingsRepository orgSettingsRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final UserMapper userMapper;

    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    @Transactional
    public AuthResponse signup(SignupRequest request) {
        String slug = request.getOrgName().toLowerCase().replaceAll("[^a-z0-9]+", "-");
        if (orgRepository.existsBySlug(slug)) {
            throw new ConflictException("Organization with this name already exists");
        }

        String orgCode = generateOrgCode(request.getOrgName());

        Organization org = Organization.builder()
                .name(request.getOrgName())
                .slug(slug)
                .orgCode(orgCode)
                .build();
        org = orgRepository.save(org);

        OrgSettings settings = OrgSettings.builder()
                .orgId(org.getId())
                .invoicePrefix(orgCode)
                .build();
        orgSettingsRepository.save(settings);

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.OWNER)
                .build();
        user.setOrgId(org.getId());
        user = userRepository.save(user);

        return buildAuthResponse(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        if (!user.isActive()) {
            throw new BadRequestException("Account is disabled");
        }

        return buildAuthResponse(user);
    }

    @Transactional
    public AuthResponse refresh(RefreshRequest request) {
        RefreshToken storedToken = refreshTokenRepository.findByTokenAndRevokedFalse(request.getRefreshToken())
                .orElseThrow(() -> new BadRequestException("Invalid refresh token"));

        if (storedToken.getExpiresAt().isBefore(Instant.now())) {
            storedToken.setRevoked(true);
            refreshTokenRepository.save(storedToken);
            throw new BadRequestException("Refresh token expired");
        }

        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        User user = userRepository.findById(storedToken.getUserId())
                .orElseThrow(() -> new BadRequestException("User not found"));

        return buildAuthResponse(user);
    }

    @Transactional
    public void logout(UUID userId) {
        refreshTokenRepository.revokeAllByUserId(userId);
    }

    private AuthResponse buildAuthResponse(User user) {
        UserPrincipal principal = new UserPrincipal(
                user.getId(), user.getOrgId(), user.getEmail(),
                user.getPassword(), user.getRole(), user.isActive()
        );

        String accessToken = tokenProvider.generateAccessToken(principal);
        String refreshTokenStr = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenStr)
                .userId(user.getId())
                .expiresAt(Instant.now().plusMillis(refreshExpirationMs))
                .build();
        refreshTokenRepository.save(refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenStr)
                .user(userMapper.toResponse(user))
                .build();
    }

    private String generateOrgCode(String name) {
        String base = name.replaceAll("[^A-Za-z]", "").toUpperCase();
        if (base.length() > 4) base = base.substring(0, 4);
        else if (base.isEmpty()) base = "ORG";

        String code = base;
        int counter = 1;
        while (orgRepository.existsByOrgCode(code)) {
            code = base + counter++;
        }
        return code;
    }
}
