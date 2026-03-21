package com.sectors.api.service;

import com.sectors.api.model.entity.Sector;
import com.sectors.api.model.dto.SectorDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SectorMapperTest {

    @Test
    void buildSectorTree() {
        Sector root2 = new Sector(2L, "Root 2", null);
        Sector child1 = new Sector(3L, "Child 1", 1L);
        Sector child2 = new Sector(4L, "Child 2", 1L);
        Sector grandChild = new Sector(5L, "Grandchild 1", 3L);
        Sector root1 = new Sector(1L, "Root 1", null);

        List<Sector> sectors = List.of(root1, root2, child1, child2, grandChild);

        List<SectorDto> result = SectorMapper.buildSectorTree(sectors);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(2L, result.get(1).id());

        List<SectorDto> root1Children = result.get(0).children();
        assertEquals(2, root1Children.size());
        assertEquals(3L, root1Children.get(0).id());
        assertEquals(4L, root1Children.get(1).id());

        List<SectorDto> child1Children = root1Children.get(0).children();
        assertEquals(1, child1Children.size());
        assertEquals(5L, child1Children.getFirst().id());

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
        Sector root = new Sector(1L, "Root", null);

        List<SectorDto> result = SectorMapper.buildSectorTree(List.of(root));

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().id());
        assertTrue(result.getFirst().children().isEmpty());
    }

    @Test
    void buildSectorTree_missingParent_treatAsRoot() {
        Sector child = new Sector(2L, "Child", 99L);

        List<SectorDto> result = SectorMapper.buildSectorTree(List.of(child));

        assertEquals(1, result.size());
        assertEquals(2L, result.getFirst().id());
        assertTrue(result.getFirst().children().isEmpty());
    }

    @Test
    void buildSectorTree_circularReference_treatAsRoot() {
        Sector selfParent = new Sector(1L, "Self", 1L);

        List<SectorDto> result = SectorMapper.buildSectorTree(List.of(selfParent));

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().id());
        assertTrue(result.getFirst().children().isEmpty());
    }
}