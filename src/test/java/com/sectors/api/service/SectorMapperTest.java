package com.sectors.api.service;

import com.sectors.api.model.entity.Sector;
import com.sectors.api.model.dto.SectorDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SectorMapperTest {

    @Test
    void buildSectorTree() {
        Sector root2 = new Sector(2L, null, 2, "Root 2");
        Sector child1 = new Sector(3L, 6L, 1, "Child 1");
        Sector child2 = new Sector(4L, 6L, 2, "Child 2");
        Sector grandChild = new Sector(5L, 3L , 1, "Grandchild 1");
        Sector root1 = new Sector(6L, null, 1, "Root 1");

        List<Sector> sectors = List.of(root1, root2, child1, child2, grandChild);

        List<SectorDto> result = SectorMapper.buildSectorTree(sectors);

        assertEquals(2, result.size());
        assertEquals(6L, result.get(0).value());
        assertEquals(2L, result.get(1).value());

        List<SectorDto> root1Children = result.get(0).children();
        assertEquals(2, root1Children.size());
        assertEquals(3L, root1Children.get(0).value());
        assertEquals(4L, root1Children.get(1).value());

        List<SectorDto> child1Children = root1Children.get(0).children();
        assertEquals(1, child1Children.size());
        assertEquals(5L, child1Children.getFirst().value());

        assertTrue(result.get(1).children().isEmpty());
    }

    @Test
    void buildSectorTree_mptyList() {
        List<Sector> sectors = List.of();

        List<SectorDto> result = SectorMapper.buildSectorTree(sectors);

        assertTrue(result.isEmpty(), "Roots should be empty for empty input");
    }

    @Test
    void buildSectorTree_singleNode() {
        Sector root = new Sector(1L, null, 1,  "Root");

        List<SectorDto> result = SectorMapper.buildSectorTree(List.of(root));

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().value());
        assertTrue(result.getFirst().children().isEmpty());
    }

    @Test
    void buildSectorTree_missingParent_treatAsRoot() {
        Sector child = new Sector(2L, 99L, 2,"Child");

        List<SectorDto> result = SectorMapper.buildSectorTree(List.of(child));

        assertEquals(1, result.size());
        assertEquals(2L, result.getFirst().value());
        assertTrue(result.getFirst().children().isEmpty());
    }

    @Test
    void buildSectorTree_circularReference_treatAsRoot() {
        Sector selfParent = new Sector(1L, 1L, 1,"Self");

        List<SectorDto> result = SectorMapper.buildSectorTree(List.of(selfParent));

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().value());
        assertTrue(result.getFirst().children().isEmpty());
    }
}