package com.billpilot.domain.auth.repository;

import com.billpilot.domain.auth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailAndOrgId(String email, UUID orgId);
    Optional<User> findByEmail(String email);
    Optional<User> findByIdAndOrgId(UUID id, UUID orgId);
    Page<User> findAllByOrgId(UUID orgId, Pageable pageable);
    boolean existsByEmailAndOrgId(String email, UUID orgId);
}
