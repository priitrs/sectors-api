package com.sectors.api.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_sectors")
@Getter
@Setter
@NoArgsConstructor
public class UserSector {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    private UUID userId;
    private Long sectorId;
    @Column(insertable = false, updatable = false)
    private Instant createdAt;
}
