package model;

import controller.gameController.GameController;
import model.resources.ResourcesTypes;
import model.technologies.Technology;
import model.technologies.TechnologyType;
import model.tiles.Tile;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class CivilizationTest {
    @Mock User user;
    @Mock Tile tile;
    @Mock City city;
    @Mock
    Technology technology;
    @Mock Map map;

    Civilization civilization = new Civilization(user,0);
    ArrayList<Tile> tiles = new ArrayList<>();
    @Test
    void setTileConditions() {
    }

    @Test
    void getColor() {


    }

    @Test
    void isInTheCivilizationsBorder() {
        assertFalse(civilization.isInTheCivilizationsBorder(tile));
        civilization.getCities().add(city);
        tiles.add(tile);
        GameController.setMap(map);
        when(city.getTiles()).thenReturn(tiles);
        assertEquals(0,civilization.getColor());
        when(map.isInRange(2,tile,tile)).thenReturn(true);
        assertTrue(civilization.isInTheCivilizationsBorder(tile));


    }

    @Test
    void changeGold() {
    }

    @Test
    void changeHappiness() {
    }

    @Test
    void getUsedLuxuryResources() {
    }

    @Test
    void getGettingResearchedTechnology() {
    }

    @Test
    void setGettingResearchedTechnology() {
    }

    @Test
    void turnOffTileConditionsBoolean() {
    }

    @Test
    void getScience() {
    }

    @Test
    void getCities() {
    }

    @Test
    void getUnits() {
    }

    @Test
    void getResearches() {
    }

    @Test
    void getUser() {
    }

    @Test
    void increaseGold() {
    }

    @Test
    void getResourcesAmount() {
    }

    @Test
    void getGold() {
    }

    @Test
    void startTheTurn() {
        civilization.getCities().add(city);
        civilization.setGettingResearchedTechnology(technology);
        civilization.getResourcesAmount().put(ResourcesTypes.GOLD,1);
        GameController.setMap(map);
        GameController.getCivilizations().add(civilization);
        civilization.startTheTurn();
        GameController.getCivilizations().remove(civilization);


    }

    @Test
    void collect() {
        civilization.getResearches();
        civilization.getHappiness();
        civilization.getGold();
        civilization.getUser();
        civilization.increaseGold(0);
        civilization.changeHappiness(1);
        civilization.increaseGold(1);
    }

    @Test
    void endTheTurn() {
    }

    @Test
    void getTileConditions() {
    }

    @Test
    void canBeTheNextResearch() {
    }

    @Test
    void getHappiness() {
    }

    @Test
    void doesContainTechnology() {
    }



    @Test
    void getSize() {
        civilization.getCities().add(city);
        when(city.getTiles()).thenReturn(tiles);
        assertEquals(0,civilization.getSize());
    }
    @Test
    void getCapital() {
        assertNull(civilization.getCapital());

    }

    @Test
    void areTechnologiesFinished() {
        assertFalse(civilization.areTechnologiesFinished());
        for (int i = 0; i <TechnologyType.values().length -1 ; i++) {
            civilization.getResearches().add(technology);
        }
        assertTrue(civilization.areTechnologiesFinished());
        when(technology.getRemainedCost()).thenReturn(1);
        assertFalse(civilization.areTechnologiesFinished());



    }
}