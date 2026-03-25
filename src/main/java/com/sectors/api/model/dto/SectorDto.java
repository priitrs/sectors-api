package com.sectors.api.model.dto;

import java.util.List;

public record SectorDto (
    Long value,
    int displayOrder,
    String title,
    List<SectorDto> children
) {
}
