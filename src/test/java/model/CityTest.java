package model;

import controller.GameController;
import model.tiles.Tile;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityTest {
    @Mock
    Tile tile;
    @Mock Civilization civilization;
    City city;
    @BeforeEach
    public void setUp() {
        Civilization.TileCondition[][] tileConditions = new Civilization.TileCondition[15][15];
        when(civilization.getTileConditions()).thenReturn(tileConditions);
        GameController.getCivilizations().add(civilization);
        city = new City(tile,"randomBS",civilization);
    }

    @Test
    void getName() {
        assertEquals("randomBS",city.getName());
    }

    @Test
    void getFounder() {
        assertEquals(civilization,city.getFounder());
    }

    @Test
    void collectFood() {
        assertEquals(city.collectFood(),4);
    }

    @Test
    void collectResources() {
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
    }

    @Test
    void getPopulation() {
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
    }

    @Test
    void getMainTile() {
    }

    @Test
    void getHP() {
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
    }

    @Test
    void checkToDestroy() {
    }

    @Test
    void addTile() {
    }

    @Test
    void destroy() {
    }

    @Test
    void changeCivilization() {
    }

    @Test
    void calculateDamage() {
    }

    @Test
    void takeDamage() {
    }

    @Test
    void setProduct() {
    }

    @Test
    void cyclesToComplete() {
    }

    @Test
    void getHalfProducedUnits() {
    }

    @Test
    void getHalfProducedBuildings() {
    }

    @Test
    void getWall() {
    }
}