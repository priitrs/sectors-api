package com.sectors.api.repository;

import com.sectors.api.model.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SectorRepository extends JpaRepository<Sector, Long> {
}
