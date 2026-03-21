package com.sectors.api.service;

import com.sectors.api.model.entity.Sector;
import com.sectors.api.model.dto.SectorDto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SectorMapper {

    static List<SectorDto> buildSectorTree(List<Sector> sectors) {
        Map<Long, SectorDto> sectorDtoMap = new HashMap<>();
        List<SectorDto> roots = new ArrayList<>();

        for (Sector s : sectors) {
            sectorDtoMap.put(s.getId(), new SectorDto(s.getId(), s.getName(), new ArrayList<>()));
        }

        for (Sector s : sectors) {
            SectorDto current = sectorDtoMap.get(s.getId());
            if (s.getParentId() == null || s.getParentId().equals(s.getId())) {
                roots.add(current);
            } else {
                SectorDto parent = sectorDtoMap.get(s.getParentId());
                if (parent != null) {
                    parent.children().add(current);
                } else {
                    roots.add(current);
                }
            }
        }

        sortRecursively(roots);

        return roots;
    }

    private static void sortRecursively(List<SectorDto> sectors) {
        sectors.sort(Comparator.comparing(SectorDto::id));
        for (SectorDto s : sectors) {
            sortRecursively(s.children());
        }
    }
}
