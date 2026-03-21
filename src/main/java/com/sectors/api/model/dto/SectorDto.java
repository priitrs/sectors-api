package com.sectors.api.model.dto;

import java.util.List;

public record SectorDto (
    Long id,
    String name,
    List<SectorDto> children
) {
}
