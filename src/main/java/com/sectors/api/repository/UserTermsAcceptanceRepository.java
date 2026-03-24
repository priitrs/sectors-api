package com.sectors.api.repository;

import com.sectors.api.model.entity.UserTermsAcceptance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserTermsAcceptanceRepository extends JpaRepository<UserTermsAcceptance, UUID> {
Optional<UserTermsAcceptance> findUserTermsAcceptanceByUserIdOrderByCreatedAtDesc(UUID userId);
}