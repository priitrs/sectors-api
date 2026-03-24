package com.sectors.api.repository;

import com.sectors.api.model.entity.UserSector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserSectorRepository extends JpaRepository<UserSector, UUID> {
    List<UserSector> findByUserId(UUID userId);

    void deleteAllByUserId(UUID userId);
}