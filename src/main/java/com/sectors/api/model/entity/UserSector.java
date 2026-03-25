package com.sectors.api.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user_sectors")
public class UserSector {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    private UUID userId;
    private Long sectorId;
    private boolean isActive;
    @Column(insertable = false, updatable = false)
    private Instant createdAt;
    @Column(insertable = false, updatable = false)
    private Instant updatedAt;
}
