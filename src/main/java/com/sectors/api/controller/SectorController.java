package com.sectors.api.controller;

import com.sectors.api.model.dto.SectorDto;
import com.sectors.api.service.SectorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "Sectors", description = "Sector operations")
@RestController
@RequestMapping("/api/sectors")
@AllArgsConstructor
public class SectorController {

    private final SectorService sectorService;

    @Operation(summary = "Get all sectors")
    @GetMapping()
    public List<SectorDto> getAllSectors() {
        return sectorService.getAllSectors();
    }
}
