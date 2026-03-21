package com.sectors.api.service;

import com.sectors.api.model.entity.Sector;
import com.sectors.api.model.dto.SectorDto;
import com.sectors.api.repository.SectorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sectors.api.service.SectorMapper.buildSectorTree;


@Service
@AllArgsConstructor
public class SectorService {

    private final SectorRepository sectorRepository;

    public List<SectorDto> getAllSectors() {
        List<Sector> sectors = sectorRepository.findAll();
        return buildSectorTree(sectors);
    }
}
