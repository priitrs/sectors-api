package com.sectors.api.model.dto;

import java.util.List;

public record SectorDto (
    Long id,
    int displayOrder,
    String name,
    List<SectorDto> children
) {
}
