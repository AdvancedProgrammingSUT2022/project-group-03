package model.tiles;

import model.City;
import model.Civilization;
import model.Units.Civilian;
import model.Units.NonCivilian;
import model.Units.UnitType;
import model.features.Feature;
import model.features.FeatureType;
import model.improvements.Improvement;
import model.improvements.ImprovementType;
import model.resources.ResourcesTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TileTest {
    @Mock Civilization civilization;
    @Mock City city;
    @Mock Civilian civilian;
    @Mock NonCivilian nonCivilian;
    @Mock Tile anotherTile;
    @Mock ResourcesTypes resourcesTypes;


    Tile tile = new Tile(TileType.FLAT,5,5);
    @Test
    void getX() {
        assertEquals(tile.getX(),5);
    }

    @Test
    void getY() {
        assertEquals(tile.getY(),5);
    }

    @Test
    void getCity() {
        tile.setCity(city);
        assertNotNull(tile.getCity());
    }

    @Test
    void getMovingPrice() {
        assertEquals(tile.getMovingPrice(),1);
        tile.setContainedFeature(new Feature(FeatureType.OASIS));
        assertEquals(tile.getMovingPrice(),2);
    }

    @Test
    void getCivilian() {
        when(civilian.getUnitType()).thenReturn(UnitType.WORKER);
        tile.setCivilian(civilian);
        assertNotNull(tile.getCivilian());
        tile.setCivilian(null);
        assertNull(tile.getCivilian());
    }

    @Test
    void getNonCivilian() {
        tile.setNonCivilian(nonCivilian);
        assertNotNull(tile.getNonCivilian());
    }

    @Test
    void getCivilization() {
        tile.setCivilization(civilization);
        assertNotNull(tile.getCivilization());
    }

    @Test
    void getNeighbours() {
        tile.setNeighbours(4,anotherTile);
        assertEquals(tile.getNeighbours(4),anotherTile);
        assertNull(tile.getNeighbours(10));
    }

    @Test
    void setTilesWithRiver() {
        tile.setTilesWithRiver(3);
        assertTrue(tile.isRiverWithNeighbour(3));
        assertFalse(tile.isRiverWithNeighbour(1));
        assertFalse(tile.isRiverWithNeighbour(-5));
    }

    @Test
    void getTileType() {
        assertEquals(tile.getTileType(),TileType.FLAT);
    }

    @Test
    void getResources() {
        tile.setResource(ResourcesTypes.FUR);
        assertEquals(tile.getResource(),ResourcesTypes.FUR);
    }

    @Test
    void isFeatureTypeValid() {
        tile = new Tile(TileType.DESERT,5,5);
        assertFalse(tile.isFeatureTypeValid(FeatureType.DELTA));
        tile.setTilesWithRiver(3);
        assertTrue(tile.isFeatureTypeValid(FeatureType.DELTA));
        assertTrue(tile.isFeatureTypeValid(FeatureType.OASIS));
        assertFalse(tile.isFeatureTypeValid(FeatureType.FOREST));
    }

    @Test
    void isResourceTypeValid() {
        tile.setContainedFeature(new Feature(FeatureType.JUNGLE));
        assertTrue(tile.isResourceTypeValid(ResourcesTypes.BANANA));
        assertFalse(tile.isResourceTypeValid(ResourcesTypes.SILK));
    }

    @Test
    void getCombatChange() {
        tile = new Tile(TileType.FLAT,5,5);
        tile.setResource(null);
        assertEquals(tile.getCombatChange(),-33);
        tile.setContainedFeature(new Feature(FeatureType.JUNGLE));
        assertEquals(tile.getCombatChange(),-8);
    }

    @Test
    void cloneTileForCivilization() {
        Tile newTile = tile.cloneTileForCivilization(civilization);
        assertEquals(tile.getImprovement(),newTile.getImprovement());
        when(resourcesTypes.isTechnologyUnlocked(any(),any())).thenReturn(true);
        newTile.setResource(resourcesTypes);
        assertEquals(newTile.cloneTileForCivilization(civilization).getImprovement(),newTile.getImprovement());
    }

    @Test
    void getImprovement() {
        Improvement improvement = new Improvement(ImprovementType.PASTURE,tile);
        tile.setImprovement(improvement);
        assertEquals(improvement,tile.getImprovement());
    }


    @Test
    void getContainedFeature() {
        tile.setContainedFeature(new Feature(FeatureType.OASIS));
        assertEquals(tile.getContainedFeature().getFeatureType(),FeatureType.OASIS);
    }

    @Test
    void getRoad() {
        Improvement road = new Improvement(ImprovementType.ROAD,tile);
        tile.setRoad(road);
        assertEquals(tile.getRoad(),road);
    }
}