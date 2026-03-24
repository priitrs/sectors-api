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
@Table(name = "user_terms_acceptance")
public class UserTermsAcceptance {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    private UUID userId;
    private boolean acceptTerms;
    @Column(insertable = false, updatable = false)
    private Instant createdAt;
}
