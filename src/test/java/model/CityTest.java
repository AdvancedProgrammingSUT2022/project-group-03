package model;

import controller.GameController;
import model.Units.Civilian;
import model.Units.NonCivilian;
import model.Units.UnitState;
import model.Units.UnitType;
import model.features.Feature;
import model.features.FeatureType;
import model.improvements.Improvement;
import model.improvements.ImprovementType;
import model.resources.ResourcesTypes;
import model.tiles.Tile;
import model.tiles.TileType;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Resources;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityTest {
    @Mock
    Civilization civilization;
    @Mock
    Civilization anotherCivilization;
    @Mock
    ResourcesTypes resourcesTypes;
    City city;
    Tile tile;
    Tile anotherTile;
    Civilian civilian;
    @BeforeEach
    public void setUp() {
        Civilization.TileCondition[][] tileConditions = new Civilization.TileCondition[15][15];
        when(civilization.getTileConditions()).thenReturn(tileConditions);
        tile = new Tile(TileType.FLAT, 5, 5);
        anotherTile = new Tile(TileType.FLAT, 5, 6);
        GameController.getCivilizations().add(civilization);
        city = new City(tile, "randomBS", civilization);
        civilian = new Civilian(tile, civilization, UnitType.SETTLER);
    }

    @Test
    void getName() {
        assertEquals("randomBS", city.getName());
    }

    @Test
    void getFounder() {
        assertEquals(civilization, city.getFounder());
    }

    @Test
    void collectFood() {
        assertEquals(city.collectFood(), 1);
        tile.setImprovement(new Improvement(ImprovementType.MINE, tile));
        assertEquals(city.collectFood(), 1);
        tile.setContainedFeature(new Feature(FeatureType.JUNGLE));
        assertEquals(city.collectFood(), 2);
        when(resourcesTypes.isTechnologyUnlocked(any(), any())).thenReturn(true);
        when(resourcesTypes.getImprovementType()).thenReturn(ImprovementType.MINE);
        tile.setResource(resourcesTypes);
        assertEquals(city.collectFood(), 2);
    }

    @Test
    void collectResources() {
//        tile.setResource(ResourcesTypes.HORSE);
//        assertEquals(city.collectResources(),);
    }

    @Test
    void collectProduction() {
    }

    @Test
    void startTheTurn() {
    }

    @Test
    void expandBorders() {
    }

    @Test
    void getGold() {

    }

    @Test
    void getProduct() {
        city.setProduct(civilian);
        assertEquals(city.getProduct(), civilian);
    }

    @Test
    void getPopulation() {
        assertEquals(city.getPopulation(), 1);
    }

    @Test
    void getCitizen() {
    }

    @Test
    void getGettingWorkedOnByCitizensTiles() {
    }

    @Test
    void getTiles() {
    }

    @Test
    void attack() {
    }

    @Test
    void isTileNeighbor() {
        assertFalse(city.isTileNeighbor(anotherTile));
        tile.setNeighbours(2, anotherTile);
        assertTrue(city.isTileNeighbor(anotherTile));
    }

    @Test
    void getMainTile() {
    }

    @Test
    void getCivilization() {
    }

    @Test
    void assignCitizenToTiles() {
    }

    @Test
    void doWeHaveEnoughMoney() {
    }

    @Test
    void getCombatStrength() {
        assertFalse(city.doWeHaveEnoughMoney(UnitType.TANK));
        when(civilization.getGold()).thenReturn(1000);
        assertTrue(city.doWeHaveEnoughMoney(UnitType.SETTLER));
    }

    @Test
    void checkToDestroy() {
        assertFalse(city.checkToDestroy());
        city.takeDamage(city.getHP());
        NonCivilian nonCivilian = new NonCivilian(tile, civilization, UnitType.SETTLER);
        tile.setNonCivilian(nonCivilian);
        nonCivilian.setState(UnitState.GARRISON);
        assertTrue(city.checkToDestroy());
    }

    @Test
    void addTile() {
        Tile thirdTile = new Tile(TileType.FLAT, 5, 7);
        assertTrue(city.addTile(thirdTile));
        anotherTile.setCivilization(anotherCivilization);
        assertFalse(city.addTile(anotherTile));
    }

    @Test
    void destroy() {
        city.destroy();
        assertNull(tile.getCity());
    }

    @Test
    void changeCivilization() {
        city.changeCivilization(anotherCivilization);
        assertEquals(city.getCivilization(), anotherCivilization);
    }

    @Test
    void calculateDamage() {
        assertEquals(city.calculateDamage(1.1), 31);
        assertEquals(city.calculateDamage(0.9), 27);
    }

    @Test
    void takeDamage() {
        int hp = city.getHP();
        city.takeDamage(2);
        assertEquals(city.getHP(), hp - 2);
    }

    @Test
    void cyclesToComplete() {
        assertEquals(3, city.cyclesToComplete(3));
        city.getGettingWorkedOnByCitizensTiles().remove(0);
        city.getMainTile().setContainedFeature(null);
        city.getMainTile().setImprovement(null);
        city.getMainTile().setResource(null);
        assertEquals(12345, city.cyclesToComplete(3));
    }

    @Test
    void getHalfProducedUnits() {
        assertEquals(0, city.getHalfProducedUnits().size());
    }

    @Test
    void getHalfProducedBuildings() {
        assertEquals(0, city.getHalfProducedBuildings().size());
    }

    @Test
    void getWall() {
        assertNull(city.getWall());
    }
}