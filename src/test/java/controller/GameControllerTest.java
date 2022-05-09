package controller;

import model.*;
import model.Units.Civilian;
import model.Units.NonCivilian;
import model.Units.UnitState;
import model.Units.UnitType;
import model.building.Building;
import model.building.BuildingType;
import model.features.Feature;
import model.features.FeatureType;
import model.improvements.Improvement;
import model.improvements.ImprovementType;
import model.resources.ResourcesTypes;
import model.technologies.Technology;
import model.technologies.TechnologyType;
import model.tiles.Tile;
import model.tiles.TileType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {
    @Mock User user;
    @Mock User user2;
    @Mock Civilian civilian;
    @Mock NonCivilian nonCivilian;
    @Mock
    City city,city2;
    @Mock Map map;

    @Mock
    Civilization civilization,civilization2;
    @Mock Tile tile;
    @Test
    void startGame() {
        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        GameController.startGame(users);
        assertNotNull(GameController.getMap());
    }

    @Test
    void getSelectedCity() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();

        when(city.getName()).thenReturn("randomName");
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        GameController.getCivilizations().add(civilization);
        ArrayList<City> cities = new ArrayList<>();
        cities.add(city);
        when(civilization.getCities()).thenReturn(cities);
        GameController.setSelectedCityByName("randomName");
        assertEquals(city,GameController.getSelectedCity());
        Civilization.TileCondition[][] tileConditions = new Civilization.TileCondition[100][100];
        tileConditions[2][3] = null;
        when(civilization.getTileConditions()).thenReturn(tileConditions);
        when(city.getMainTile()).thenReturn(tile);
        GameController.mapShowCityName("randomName");
        tileConditions[2][3] = new Civilization.TileCondition(tile,true);
        GameController.mapShowCityName("randomName");
        GameController.mapShowCityName("bla bla bla");
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        GameController.setSelectedCityByPosition(2,3);
        assertEquals(city,GameController.getSelectedCity());
        tile.setCity(city);
        GameController.setSelectedCityByPosition(2,3);
        assertEquals(city,GameController.getSelectedCity());
        GameController.setSelectedCityByName("bla bla bla");
        assertEquals(city,GameController.getSelectedCity());
    }

    @Test
    void setSelectedCombatUnit() {
        GameController.setSelectedUnit(null);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        GameController.setSelectedNonCivilian(2,3);
        assertNull(GameController.getSelectedUnit());
        NonCivilian unit = new NonCivilian(tile,civilization, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        GameController.setSelectedNonCivilian(2,3);
        assertEquals(GameController.getSelectedUnit(),unit);
    }

    @Test
    void setSelectedNonCombatUnit() {
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        GameController.setSelectedCivilian(2,3);
        GameController.setSelectedUnit(null);
        assertNull(GameController.getSelectedUnit());
        Civilian unit = new Civilian(tile,civilization, UnitType.SETTLER);
        tile.setCivilian(unit);
        GameController.setSelectedCivilian(2,3);
        assertEquals(GameController.getSelectedUnit(),unit);
    }

    @Test
    void reAssignCitizen() {
        GameController.setMap(map);
        assertEquals(2,GameController.assignCitizen(3,3));
        assertEquals(2,GameController.reAssignCitizen(3,3,3,3));
        GameController.setSelectedCity(null);
        assertEquals(3,GameController.assignCitizen(3,3));
        assertEquals(3,GameController.reAssignCitizen(3,3,3,3));
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        when(city.getName()).thenReturn("randomName");
        GameController.getCivilizations().add(civilization);
        ArrayList<City> cities = new ArrayList<>();
        cities.add(city);
        when(civilization.getCities()).thenReturn(cities);
        when(city.getCivilization()).thenReturn(civilization);
        GameController.setSelectedCityByName("randomName");
        assertEquals(1,GameController.assignCitizen(3,-4));
        assertEquals(1,GameController.reAssignCitizen(3,3,3,-4));
        when(map.getX()).thenReturn(100);
        when(map.getY()).thenReturn(100);
        assertEquals(4,GameController.assignCitizen(3,3));
        assertEquals(4,GameController.reAssignCitizen(3,3,3,3));
    }

    @Test
    void deleteFromUnfinishedTasks() {
        if (GameController.getUnfinishedTasks().size() > 0)
            GameController.getUnfinishedTasks().subList(0, GameController.getUnfinishedTasks().size()).clear();
        GameController.getUnfinishedTasks().add(new Tasks(tile,TaskTypes.UNIT));
        GameController.deleteFromUnfinishedTasks(new Tasks(tile,TaskTypes.UNIT));
        assertEquals(GameController.getUnfinishedTasks().size(),0);
    }

    @Test
    void unitMoveTo() {
    }

    @Test
    void unitSleep() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.setSelectedUnit(null);
        assertEquals(1,GameController.unitSleep());
        GameController.getCivilizations().add(civilization);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        GameController.setSelectedNonCivilian(2,3);
        assertEquals(2,GameController.unitSleep());

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        NonCivilian unit2 = new NonCivilian(tile2,civilization, UnitType.RIFLEMAN);
        tile2.setNonCivilian(unit2);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        GameController.setSelectedNonCivilian(2,4);
        assertEquals(0,GameController.unitSleep());
    }

    @Test
    void unitAlert() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.setSelectedUnit(null);
        assertEquals(1,GameController.unitAlert());
        GameController.getCivilizations().add(civilization);
        when(civilization.getTileConditions()).thenReturn(new Civilization.TileCondition[15][15]);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        GameController.setSelectedNonCivilian(2,3);
        assertEquals(2,GameController.unitAlert());

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        NonCivilian unit2 = new NonCivilian(tile2,civilization, UnitType.RIFLEMAN);
        tile2.setNonCivilian(unit2);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        GameController.setSelectedNonCivilian(2,4);
        assertEquals(0,GameController.unitAlert());
        tile2.setNeighbours(1,tile);
        tile.setNeighbours(2,tile2);
        assertEquals(3,GameController.unitAlert());
    }

    @Test
    void unitFortify() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();

        GameController.setSelectedUnit(null);
        assertEquals(1,GameController.unitChangeState(0));
        GameController.getCivilizations().add(civilization);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        GameController.setSelectedNonCivilian(2,3);
        assertEquals(2,GameController.unitChangeState(0));

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        Civilian unit2 = new Civilian(tile2,civilization, UnitType.SETTLER);
        tile2.setCivilian(unit2);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        GameController.setSelectedCivilian(2,4);
        assertEquals(3,GameController.unitChangeState(0));

        NonCivilian unit3 = new NonCivilian(tile2,civilization, UnitType.RIFLEMAN);
        tile2.setNonCivilian(unit3);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        GameController.setSelectedNonCivilian(2,4);
        assertEquals(0,GameController.unitChangeState(0));
        assertEquals(0,GameController.unitChangeState(1));
        assertEquals(0,GameController.unitChangeState(2));
        assertEquals(0,GameController.unitChangeState(3));
    }

    @Test
    void unitSetupRanged() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();

        GameController.setSelectedUnit(null);
        assertEquals(1,GameController.unitSetupRanged());
        GameController.getCivilizations().add(civilization);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.ARCHER);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        GameController.setSelectedNonCivilian(2,3);
        assertEquals(2,GameController.unitSetupRanged());

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        Civilian unit4 = new Civilian(tile2,civilization, UnitType.SETTLER);
        tile2.setCivilian(unit4);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        GameController.setSelectedCivilian(2,4);
        assertEquals(3,GameController.unitSetupRanged());

        NonCivilian unit3 = new NonCivilian(tile2,civilization, UnitType.CANON);
        tile2.setNonCivilian(unit3);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        GameController.setSelectedNonCivilian(2,4);
        assertEquals(0,GameController.unitSetupRanged());

    }

    @Test
    void unitFoundCity() {
    }

    @Test
    void unitCancelMission() {
    }

    @Test
    void unitDelete() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        assertEquals(1,GameController.unitDelete(null));
        GameController.getCivilizations().add(civilization);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        GameController.setMap(map);
        assertEquals(2,GameController.unitDelete(unit));

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        NonCivilian unit2 = new NonCivilian(tile2,civilization, UnitType.RIFLEMAN);
        tile2.setNonCivilian(unit2);
        when(civilization.getTileConditions()).thenReturn(new Civilization.TileCondition[40][40]);
        assertEquals(0,GameController.unitDelete(unit2));

        Civilian unit3 = new Civilian(tile2,civilization, UnitType.SETTLER);
        tile2.setNonCivilian(unit2);
        assertEquals(0,GameController.unitDelete(unit3));
    }

    @Test
    void unitBuild() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.setSelectedUnit(null);
        assertEquals(1,GameController.unitBuild(ImprovementType.PASTURE));
        GameController.getCivilizations().add(civilization);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        GameController.setSelectedNonCivilian(2,3);
        assertEquals(2,GameController.unitBuild(ImprovementType.PASTURE));

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        NonCivilian unit2 = new NonCivilian(tile2,civilization, UnitType.RIFLEMAN);
        tile2.setNonCivilian(unit2);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        GameController.setSelectedNonCivilian(2,4);
        assertEquals(3,GameController.unitBuild(ImprovementType.PASTURE));
        Civilian civilian = new Civilian(tile2, civilization, UnitType.WORKER);
        tile2.setCivilian(civilian);
        GameController.setSelectedCivilian(2,4);
        assertEquals(4,GameController.unitBuild(ImprovementType.PASTURE));
        tile2.setImprovement(new Improvement(ImprovementType.PASTURE,tile2));
        tile2.setContainedFeature(null);
        when(civilization.doesContainTechnology(TechnologyType.ANIMAL_HUSBANDRY)).thenReturn(1);
        assertEquals(5,GameController.unitBuild(ImprovementType.PASTURE));
        tile2.getImprovement().setNeedsRepair(4);
        tile2.setCivilization(civilization);
        assertEquals(0,GameController.unitBuild(ImprovementType.PASTURE));
        tile2.setContainedFeature(new Feature(FeatureType.JUNGLE));
        assertEquals(0,GameController.unitBuild(ImprovementType.PASTURE));
    }

    @Test
    void doesHaveTheRequiredTechnologyToBuildImprovement() {
    }

    @Test
    void unitBuildRoad() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.setSelectedUnit(null);
        assertEquals(1,GameController.unitBuildRoad());
        GameController.getCivilizations().add(civilization);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        GameController.setSelectedNonCivilian(2,3);
        assertEquals(2,GameController.unitBuildRoad());

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        NonCivilian unit2 = new NonCivilian(tile2,civilization, UnitType.RIFLEMAN);
        tile2.setNonCivilian(unit2);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        GameController.setSelectedNonCivilian(2,4);
        assertEquals(3,GameController.unitBuildRoad());

        Civilian civilian = new Civilian(tile2, civilization, UnitType.WORKER);
        tile2.setCivilian(civilian);
        GameController.setSelectedCivilian(2,4);
        assertEquals(0,GameController.unitBuildRoad());
        assertEquals(6,GameController.unitBuildRoad());
    }

    @Test
    void unitBuildRailRoad() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.setSelectedUnit(null);
        assertEquals(1,GameController.unitBuildRailRoad());
        GameController.getCivilizations().add(civilization);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        GameController.setSelectedNonCivilian(2,3);
        assertEquals(2,GameController.unitBuildRailRoad());

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        NonCivilian unit2 = new NonCivilian(tile2,civilization, UnitType.RIFLEMAN);
        tile2.setNonCivilian(unit2);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        GameController.setSelectedNonCivilian(2,4);
        assertEquals(3,GameController.unitBuildRailRoad());

        Civilian civilian = new Civilian(tile2, civilization, UnitType.WORKER);
        tile2.setCivilian(civilian);
        GameController.setSelectedCivilian(2,4);
        assertEquals(4,GameController.unitBuildRailRoad());
        when(civilization.doesContainTechnology(TechnologyType.RAILROAD)).thenReturn(1);
        assertEquals(0,GameController.unitBuildRailRoad());
        assertEquals(6,GameController.unitBuildRailRoad());
    }

    @Test
    void unitRemoveFromTile() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.setSelectedUnit(null);
        assertEquals(1,GameController.unitRemoveFromTile(false));
        assertEquals(1,GameController.unitRemoveFromTile(true));
        GameController.getCivilizations().add(civilization);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        GameController.setSelectedNonCivilian(2,3);
        assertEquals(2,GameController.unitRemoveFromTile(false));
        assertEquals(2,GameController.unitRemoveFromTile(true));

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        NonCivilian unit2 = new NonCivilian(tile2,civilization, UnitType.RIFLEMAN);
        tile2.setNonCivilian(unit2);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        GameController.setSelectedNonCivilian(2,4);
        assertEquals(3,GameController.unitRemoveFromTile(false));
        Civilian civilian = new Civilian(tile2, civilization, UnitType.WORKER);
        tile2.setCivilian(civilian);
        GameController.setSelectedCivilian(2,4);
        tile2.setContainedFeature(new Feature(FeatureType.DELTA));
        assertEquals(4,GameController.unitRemoveFromTile(true));
        assertEquals(5,GameController.unitRemoveFromTile(false));
        tile2.setRoad(new Improvement(ImprovementType.ROAD,tile2));
        assertEquals(0,GameController.unitRemoveFromTile(false));
        tile2.setContainedFeature(new Feature(FeatureType.FOREST));
        when(civilization.getTileConditions()).thenReturn(new Civilization.TileCondition[30][30]);
        assertEquals(0,GameController.unitRemoveFromTile(true));
    }

    @Test
    void unitRepair() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.setSelectedUnit(null);
        assertEquals(1,GameController.unitRepair());
        GameController.getCivilizations().add(civilization);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        GameController.setSelectedNonCivilian(2,3);
        assertEquals(2,GameController.unitRepair());

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        NonCivilian unit2 = new NonCivilian(tile2,civilization, UnitType.RIFLEMAN);
        tile2.setNonCivilian(unit2);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        GameController.setSelectedNonCivilian(2,4);
        assertEquals(3,GameController.unitRepair());
        Civilian civilian = new Civilian(tile2, civilization, UnitType.WORKER);
        tile2.setCivilian(civilian);
        GameController.setSelectedCivilian(2,4);
        assertEquals(4,GameController.unitRepair());
        tile2.setImprovement(new Improvement(ImprovementType.PASTURE,tile2));
        assertEquals(5,GameController.unitRepair());
        tile2.getImprovement().setNeedsRepair(4);
        assertEquals(0,GameController.unitRepair());
    }

    @Test
    void mapShowPosition() {
        GameController.mapShowPosition(-10,-10);
        GameController.mapShowPosition(200,200);
    }

    @Test
    void mapShowCityName() {
    }

    @Test
    void mapMove() {
        GameController.mapMove(4,"r");
        GameController.mapMove(4,"l");
        GameController.mapMove(4,"u");
        GameController.mapMove(4,"d");
    }

    @Test
    void buyTile() {
        assertEquals(2,GameController.buyTile(-2,-2));
        when(map.getX()).thenReturn(100);
        when(map.getY()).thenReturn(100);
        GameController.setMap(map);
        assertEquals(4,GameController.buyTile(10,10));
        GameController.setSelectedCity(city);
        when(city.getCivilization()).thenReturn(GameController.getCivilizations().get(0));
        assertEquals(3,GameController.buyTile(10,10));
        when(city.isTileNeighbor(any())).thenReturn(true);
        assertEquals(1,GameController.buyTile(10,10));
        when(city.addTile(any())).thenReturn(true);
        assertEquals(0,GameController.buyTile(10,10));
    }

    @Test
    void buildWall() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        assertEquals(1,GameController.buildWall());
        when(city.getName()).thenReturn("randomName");
        GameController.getCivilizations().add(civilization);
        ArrayList<City> cities = new ArrayList<>();
        cities.add(city);
        when(civilization.getCities()).thenReturn(cities);
        GameController.setSelectedCityByName("randomName");
        assertEquals(2,GameController.buildWall());
        when(city.getCivilization()).thenReturn(civilization);
        assertEquals(0,GameController.buildWall());
        Building wall = new Building(BuildingType.WALL);
        ArrayList<Building> walls = new ArrayList<>();
        walls.add(wall);
        when(city.getHalfProducedBuildings()).thenReturn(walls);
        assertEquals(0,GameController.buildWall());
        wall.setRemainedCost(0);
        when(city.getWall()).thenReturn(wall);
        assertEquals(3,GameController.buildWall());
    }

    @Test
    void nextTurnIfYouCan() {
        assertFalse(GameController.nextTurnIfYouCan());
        if (GameController.getUnfinishedTasks().size() > 0)
            GameController.getUnfinishedTasks().subList(0, GameController.getUnfinishedTasks().size()).clear();
//        if (GameController.getCivilizations().size() > 0)
//            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
//        GameController.setSelectedUnit(null);
//        GameController.getCivilizations().add(civilization);
//        GameController.getCivilizations().add(civilization2);

//        civilization.getCities().add(city2);
        assertTrue(GameController.nextTurnIfYouCan());
    }

    @Test
    void nextTurn() {
    }

    @Test
    void setUnfinishedTasks() {
    }

    @Test
    void cheatUnit() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.setSelectedUnit(null);
        GameController.getCivilizations().add(civilization);
        GameController.getCivilizations().add(civilization2);

        when(civilization.getTileConditions()).thenReturn(new Civilization.TileCondition[50][50]);
        Map map2 = new Map(GameController.getCivilizations());
        GameController.setMap(map2);

        GameController.getCivilizations().add(civilization);
        when(civilization.getTileConditions()).thenReturn(new Civilization.TileCondition[15][15]);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        GameController.setSelectedNonCivilian(2,3);
        assertEquals(0,GameController.cheatUnit(2,3,UnitType.SETTLER));
        assertEquals(2,GameController.cheatUnit(2,3,UnitType.SETTLER));
        assertEquals(2,GameController.cheatUnit(2,3,UnitType.SPEARMAN));
    }

    @Test
    void openNewArea() {
    }

    @Test
    void getMap() {
    }

    @Test
    void printMap() {
    }

    @Test
    void getSelectedUnit() {
    }

    @Test
    void startProducingUnit() {
    }

    @Test
    void getCivilizations() {
    }

    @Test
    void getPlayerTurn() {
        assertEquals(0,GameController.getPlayerTurn());
    }

    @Test
    void getUnfinishedTasks() {
    }

    @Test
    void unitAttack() {
    }

    @Test
    void cityAttack() {
    }

    @Test
    void cityDestiny() {
    }

    @Test
    void cheatScience() {
        GameController.cheatScience(10);
    }

    @Test
    void cheatProduction() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.setSelectedCity(null);
        when(city.getName()).thenReturn("randomName");
        GameController.getCivilizations().add(civilization);
        ArrayList<City> cities = new ArrayList<>();
        cities.add(city);
        when(civilization.getCities()).thenReturn(cities);
        assertEquals(1,GameController.cheatProduction(3));
        GameController.setSelectedCityByName("randomName");
        assertEquals(city,GameController.getSelectedCity());
        assertEquals(2,GameController.cheatProduction(3));
        when(city.getCivilization()).thenReturn(civilization);
        assertEquals(0,GameController.cheatProduction(3));
    }

    @Test
    void cheatResource() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        HashMap<ResourcesTypes, Integer> resource = new HashMap<>();
        resource.put(ResourcesTypes.GEMSTONE,3);
        GameController.getCivilizations().add(civilization);
        when(civilization.getResourcesAmount()).thenReturn(resource);
        GameController.cheatResource(4, ResourcesTypes.GEMSTONE);
    }

    @Test
    void unitPillage() {
    }

    @Test
    void skipUnitTask() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.setSelectedUnit(null);
        assertEquals(1,GameController.skipUnitTask());
        GameController.getCivilizations().add(civilization);
        when(civilization.getTileConditions()).thenReturn(new Civilization.TileCondition[100][100]);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        GameController.setSelectedNonCivilian(2,3);
        assertEquals(2,GameController.skipUnitTask());

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        NonCivilian unit2 = new NonCivilian(tile2,civilization, UnitType.RIFLEMAN);
        tile2.setNonCivilian(unit2);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        GameController.setSelectedNonCivilian(2,4);
        assertEquals(0,GameController.unitAlert());
        tile2.setNeighbours(1,tile);
        tile.setNeighbours(2,tile2);
        assertEquals(3,GameController.skipUnitTask());
        GameController.getUnfinishedTasks().add(new Tasks(tile2,TaskTypes.UNIT));
        assertEquals(0,GameController.skipUnitTask());
        tile2.setCivilian(new Civilian(tile2,civilization2,UnitType.SETTLER));
        tile.setCivilian(new Civilian(tile,civilization2,UnitType.SETTLER));
        GameController.getSelectedUnit().setState(UnitState.ALERT);
        unit2.setState(UnitState.ALERT);
        assertEquals(3,GameController.unitAlert());
    }

    @Test
    void cheatTechnology() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        assertEquals(1,GameController.cheatTechnology(null));
        Civilization civilization3 = new Civilization(user, 3);
        GameController.getCivilizations().add(civilization3);
        GameController.getCivilizations().add(civilization2);
        Map map2 = new Map(GameController.getCivilizations());
        GameController.setMap(map2);
        assertEquals(0,GameController.cheatTechnology(TechnologyType.TELEGRAPH));
        Technology technology = new Technology(TechnologyType.ELECTRICITY);
        technology.setRemainedCost(5);
        civilization3.getResearches().add(technology);
        assertEquals(2,GameController.cheatTechnology(TechnologyType.TELEGRAPH));
        assertEquals(0,GameController.cheatTechnology(TechnologyType.ELECTRICITY));
        technology.setRemainedCost(0);
        assertEquals(2,GameController.cheatTechnology(TechnologyType.ELECTRICITY));
    }

    @Test
    void openMap() {
        ArrayList<Civilization> civilizations = new ArrayList<>();
        civilizations.add(civilization);
        civilizations.add(civilization2);
        Map map2 = new Map(civilizations);
        GameController.setMap(map2);
        GameController.openMap();
    }

    @Test
    void cheatMoveIt() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.setSelectedUnit(null);
        assertEquals(1,GameController.cheatMoveIt(2,3));
        GameController.getCivilizations().add(civilization);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        GameController.setSelectedNonCivilian(2,3);
        assertEquals(2,GameController.cheatMoveIt(2,3));

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        NonCivilian unit2 = new NonCivilian(tile2,civilization, UnitType.RIFLEMAN);
        Civilian civilian = new Civilian(tile2,civilization, UnitType.SETTLER);
        tile2.setNonCivilian(unit2);
        tile2.setCivilian(civilian);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        GameController.setSelectedNonCivilian(2,4);
        assertEquals(3,GameController.cheatMoveIt(-10,-10));
        when(map.getX()).thenReturn(100);
        when(map.getY()).thenReturn(100);
        assertEquals(0,GameController.cheatMoveIt(2,4));
        GameController.setSelectedCivilian(2,4);
        assertEquals(0,GameController.cheatMoveIt(2,4));
    }

    @Test
    void cheatCaptureCity() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        when(city.getName()).thenReturn("randomName");
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        GameController.getCivilizations().add(civilization);
        ArrayList<City> cities = new ArrayList<>();
        cities.add(city);
        when(civilization.getCities()).thenReturn(cities);
        assertEquals(1, GameController.cheatCaptureCity("bla bla"));
        assertEquals(0, GameController.cheatCaptureCity("randomName"));
        when(city.getCivilization()).thenReturn(civilization);
        assertEquals(2, GameController.cheatCaptureCity("randomName"));
    }

    @Test
    void buyUnit() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.getCivilizations().add(civilization);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        assertEquals(1,GameController.buyUnit("asghar",58,96));
        when(GameController.getMap().coordinatesToTile(2, 3)).thenReturn(tile);
        assertEquals(2,GameController.buyUnit("archer",2,3));
        tile.setCivilization(civilization);
        assertEquals(3,GameController.buyUnit("archer",2,3));
        when(civilian.getUnitType()).thenReturn(UnitType.SETTLER);
        tile.setCivilian(civilian);
        tile.setNonCivilian(nonCivilian);
        when(civilization.getGold()).thenReturn(10000);
        assertEquals(4,GameController.buyUnit("settler",2,3));
        assertEquals(4,GameController.buyUnit("archer",2,3));
        tile.setNonCivilian(null);
        Civilization.TileCondition[][] tileConditions = new Civilization.TileCondition[40][40];
        when(civilization.getTileConditions()).thenReturn(tileConditions);
        assertEquals(0,GameController.buyUnit("archer",2,3));
    }

    @Test
    void cheatRoadEverywhere() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.getCivilizations().add(civilization);
        GameController.getCivilizations().add(civilization2);

        when(civilization.getUser()).thenReturn(user);
        when(civilization.getTileConditions()).thenReturn(new Civilization.TileCondition[50][50]);
        when(user.getNickname()).thenReturn("nick");
        Map map2 = new Map(GameController.getCivilizations());
        GameController.setMap(map2);
        GameController.cheatRoadEverywhere();
        assertNotNull(GameController.printMap());
    }
}