package model;

import controller.gameController.CheatCommandsController;
import controller.gameController.GameController;
import model.Units.Civilian;
import model.Units.NonCivilian;
import model.Units.UnitType;
import model.features.Feature;
import model.features.FeatureType;
import model.improvements.Improvement;
import model.improvements.ImprovementType;
import model.technologies.TechnologyType;
import model.tiles.Tile;
import model.tiles.TileType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;

import static controller.gameController.GameController.getMap;
import static controller.gameController.GameController.openNewArea;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapTest {

    @Test
    void addStartingSettlers() {
    }


    @Test
    void isInRange() {
        civilization = new Civilization(i,1);
        Civilization civilization1 = new Civilization(i,1);
        ArrayList<Civilization> arrayList= new ArrayList<>();
        arrayList.add(civilization);
        arrayList.add(civilization1);
        Map map = new Map(arrayList);
        map.getTiles()[8][2].setContainedFeature(new Feature(FeatureType.FOREST));
        NonCivilian unit = new NonCivilian(map.getTiles()[8][1],civilization, UnitType.PANZER);
        map.getTiles()[8][1].setNonCivilian(unit);
        civilization.setTileConditions
                (new Civilization.TileCondition[map.getX()][map.getY()]);
        for(int i = 0;i<map.getX();i++)
            for(int j=0;j<map.getY();j+=2)
                openNewArea(map.coordinatesToTile(i,j),civilization,null);
        try (MockedStatic<GameController> utilities = Mockito.mockStatic(GameController.class)) {
            utilities.when(GameController::getMap).thenReturn(map);
            map.isInRange(2,map.getTiles()[8][1],map.getTiles()[10][1]);
            assertEquals(map.getTiles()[0][0],map.coordinatesToTile(0,0));
        }
        arrayList.add(civilization1);
        arrayList.add(civilization1);
        map = new Map(arrayList);
        arrayList.add(civilization1);
        map = new Map(arrayList);


    }



    @Test
    void getY() {
    }
    @Mock
    User i;
    Civilization civilization;
    @Test
    void findNextTile() {
        civilization = new Civilization(i,1);
        Civilization civilization1 = new Civilization(i,1);
        ArrayList<Civilization> arrayList= new ArrayList<>();
        arrayList.add(civilization);
        arrayList.add(civilization1);
        Map map = new Map(arrayList);
        map.getTiles()[8][1].setTileType(TileType.GRASSLAND);
        map.getTiles()[8][1].setContainedFeature(null);
        map.getTiles()[7][1].setTileType(TileType.TUNDRA);
        map.getTiles()[7][1].setContainedFeature(null);
        map.getTiles()[6][1].setTileType(TileType.GRASSLAND);
        map.getTiles()[6][1].setContainedFeature(null);
        map.getTiles()[8][2].setTileType(TileType.TUNDRA);
        map.getTiles()[8][2].setContainedFeature(new Feature(FeatureType.FOREST));
        NonCivilian unit = new NonCivilian(map.getTiles()[8][1],civilization, UnitType.PANZER);
        map.getTiles()[8][1].setNonCivilian(unit);
        civilization.setTileConditions
                (new Civilization.TileCondition[map.getX()][map.getY()]);

        try (MockedStatic<GameController> utilities = Mockito.mockStatic(GameController.class)) {
            utilities.when(GameController::getMap).thenReturn(map);
            unit.move(map.getTiles()[6][1],true);
            for(int i = 0;i<map.getX();i++)
                for(int j=0;j<map.getY();j+=2)
                    openNewArea(map.coordinatesToTile(i,j),civilization,null);
            unit.move(map.getTiles()[6][1],true);


        }


    }

    @Test
    void printMap() {
        ArrayList<User> usersList = new ArrayList<>();
        usersList.add(new User("s","s","s"));
        usersList.add(new User("s","s","s"));
        GameController.startGame(usersList);
        GameController.printMap();
        CheatCommandsController.openMap();
        assertTrue(GameController.printMap().length() > 10);
        GameController.getMap().getTiles()[0][0].setCivilization(GameController.getCivilizations().get(0));
        GameController.getMap().getTiles()[0][1].setCivilization(GameController.getCivilizations().get(0));
        GameController.getMap().getTiles()[0][2].setCivilization(GameController.getCivilizations().get(0));
        GameController.getMap().getTiles()[4][4]
                .setNonCivilian(
                        new NonCivilian(GameController.getMap().getTiles()[4][4]
                                ,GameController.getCivilizations().get(1),UnitType.ARCHER));
        GameController.getMap().getTiles()[5][4].setCivilian(new Civilian(GameController.getMap().getTiles()[5][4],GameController.getCivilizations().get(0),UnitType.SETTLER));
        GameController.getMap().getTiles()[5][4].setNonCivilian(new NonCivilian(GameController.getMap().getTiles()[5][4],GameController.getCivilizations().get(1),UnitType.ARCHER));
        GameController.getMap().getTiles()[4][4].setCivilian(new Civilian(GameController.getMap().coordinatesToTile(4,4),GameController.getCivilizations().get(0),UnitType.SETTLER));
        CheatCommandsController.cheatTechnology(TechnologyType.MINING);
        CheatCommandsController.cheatTechnology(TechnologyType.ANIMAL_HUSBANDRY);
        CheatCommandsController.cheatTechnology(TechnologyType.AGRICULTURE);
        GameController.getMap().getTiles()[4][4].setRoad(new Improvement(ImprovementType.ROAD,GameController.getMap().getTiles()[4][4]));
        GameController.getMap().getTiles()[4][4].setImprovement(new Improvement(ImprovementType.FARM,GameController.getMap().getTiles()[4][4]));
        CheatCommandsController.openMap();
        GameController.getMap().printMap(GameController.getCivilizations().get(0).getTileConditions(),0,0);
        GameController.getMap().printMap(GameController.getCivilizations().get(0).getTileConditions(),1,1);



    }

    @Test
    void getTiles() {
    }

    @Test
    void testAddStartingSettlers() {
    }

    @Test
    void testCoordinatesToTile() {
    }

    @Test
    void testIsInRange() {
    }

    @Test
    void testGetX() {
    }

    @Test
    void testGetY() {
    }

    @Test
    void testFindNextTile() {
    }

}