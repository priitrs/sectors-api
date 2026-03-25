package com.sectors.api.repository;

import com.sectors.api.model.entity.UserSector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserSectorRepository extends JpaRepository<UserSector, UUID> {
    List<UserSector> findByUserId(UUID userId);

    @Modifying
    @Query("UPDATE UserSector us SET us.isActive = false WHERE us.userId = :userId AND us.isActive = true")
    void deactivateAllActiveByUserId(@Param("userId") UUID userId);
}