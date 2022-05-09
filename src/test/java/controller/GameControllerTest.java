package controller;

import controller.gameController.*;
import model.*;
import model.Units.*;
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
    City city;
    @Mock Map map;
    @Mock NonCivilian nonCivilian1;
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
        TileXAndYFlagSelectUnitController.setSelectedCityByName("randomName");
        assertEquals(city,GameController.getSelectedCity());
        Civilization.TileCondition[][] tileConditions = new Civilization.TileCondition[100][100];
        tileConditions[2][3] = null;
        when(civilization.getTileConditions()).thenReturn(tileConditions);
        when(city.getMainTile()).thenReturn(tile);
        MapCommandsController.mapShowCityName("randomName");
        tileConditions[2][3] = new Civilization.TileCondition(tile,true);
        MapCommandsController.mapShowCityName("randomName");
        MapCommandsController.mapShowCityName("bla bla bla");
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        TileXAndYFlagSelectUnitController.setSelectedCityByPosition(2,3);
        assertEquals(city,GameController.getSelectedCity());
        tile.setCity(city);
        TileXAndYFlagSelectUnitController.setSelectedCityByPosition(2,3);
        assertEquals(city,GameController.getSelectedCity());
        TileXAndYFlagSelectUnitController.setSelectedCityByName("bla bla bla");
        assertEquals(city,GameController.getSelectedCity());
    }

    @Test
    void setSelectedCombatUnit() {
        GameController.setSelectedUnit(null);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,3);
        assertNull(GameController.getSelectedUnit());
        NonCivilian unit = new NonCivilian(tile,civilization, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,3);
        assertEquals(GameController.getSelectedUnit(),unit);
    }

    @Test
    void setSelectedNonCombatUnit() {
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        TileXAndYFlagSelectUnitController.setSelectedCivilian(2,3);
        GameController.setSelectedUnit(null);
        assertNull(GameController.getSelectedUnit());
        Civilian unit = new Civilian(tile,civilization, UnitType.SETTLER);
        tile.setCivilian(unit);
        TileXAndYFlagSelectUnitController.setSelectedCivilian(2,3);
        assertEquals(GameController.getSelectedUnit(),unit);
    }

    @Test
    void reAssignCitizen() {
        GameController.setMap(map);
        assertEquals(2, CityCommandsController.assignCitizen(3,3));
        assertEquals(2, CityCommandsController.reAssignCitizen(3,3,3,3));
        GameController.setSelectedCity(null);
        assertEquals(3, CityCommandsController.assignCitizen(3,3));
        assertEquals(3, CityCommandsController.reAssignCitizen(3,3,3,3));
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        when(city.getName()).thenReturn("randomName");
        GameController.getCivilizations().add(civilization);
        ArrayList<City> cities = new ArrayList<>();
        cities.add(city);
        when(civilization.getCities()).thenReturn(cities);
        when(city.getCivilization()).thenReturn(civilization);
        TileXAndYFlagSelectUnitController.setSelectedCityByName("randomName");
        assertEquals(1, CityCommandsController.assignCitizen(3,-4));
        assertEquals(1, CityCommandsController.reAssignCitizen(3,3,3,-4));
        when(map.getX()).thenReturn(100);
        when(map.getY()).thenReturn(100);
        assertEquals(4, CityCommandsController.assignCitizen(3,3));
        assertEquals(4, CityCommandsController.reAssignCitizen(3,3,3,3));
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
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.getCivilizations().add(civilization);
        GameController.setSelectedUnit(civilian);
        GameController.setMap(map);
        when(map.getY()).thenReturn(100);
        when(map.getX()).thenReturn(100);
        assertFalse(UnitStateController.unitMoveTo(0, 0));
        when(civilian.getCivilization()).thenReturn(civilization);
        when(map.coordinatesToTile(0,0)).thenReturn(tile);
        assertFalse(UnitStateController.unitMoveTo(0, 0));
    }

    @Test
    void unitSleep() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.setSelectedUnit(null);
        assertEquals(1, UnitStateController.unitSleep());
        GameController.getCivilizations().add(civilization);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,3);
        assertEquals(2, UnitStateController.unitSleep());

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        NonCivilian unit2 = new NonCivilian(tile2,civilization, UnitType.RIFLEMAN);
        tile2.setNonCivilian(unit2);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,4);
        assertEquals(0, UnitStateController.unitSleep());
    }

    @Test
    void unitAlert() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.setSelectedUnit(null);
        assertEquals(1, UnitStateController.unitAlert());
        GameController.getCivilizations().add(civilization);
        when(civilization.getTileConditions()).thenReturn(new Civilization.TileCondition[15][15]);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,3);
        assertEquals(2, UnitStateController.unitAlert());

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        NonCivilian unit2 = new NonCivilian(tile2,civilization, UnitType.RIFLEMAN);
        tile2.setNonCivilian(unit2);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,4);
        assertEquals(0, UnitStateController.unitAlert());
        tile2.setNeighbours(1,tile);
        tile.setNeighbours(2,tile2);
        assertEquals(3, UnitStateController.unitAlert());
    }

    @Test
    void unitFortify() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();

        GameController.setSelectedUnit(null);
        assertEquals(1, UnitStateController.unitChangeState(0));
        GameController.getCivilizations().add(civilization);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,3);
        assertEquals(2, UnitStateController.unitChangeState(0));

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        Civilian unit2 = new Civilian(tile2,civilization, UnitType.SETTLER);
        tile2.setCivilian(unit2);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        TileXAndYFlagSelectUnitController.setSelectedCivilian(2,4);
        assertEquals(3, UnitStateController.unitChangeState(0));

        NonCivilian unit3 = new NonCivilian(tile2,civilization, UnitType.RIFLEMAN);
        tile2.setNonCivilian(unit3);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,4);
        assertEquals(0, UnitStateController.unitChangeState(0));
        assertEquals(0, UnitStateController.unitChangeState(1));
        assertEquals(0, UnitStateController.unitChangeState(2));
        assertEquals(0, UnitStateController.unitChangeState(3));
    }

    @Test
    void unitSetupRanged() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();

        GameController.setSelectedUnit(null);
        assertEquals(1, UnitStateController.unitSetupRanged());
        GameController.getCivilizations().add(civilization);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.ARCHER);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,3);
        assertEquals(2, UnitStateController.unitSetupRanged());

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        Civilian unit4 = new Civilian(tile2,civilization, UnitType.SETTLER);
        tile2.setCivilian(unit4);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        TileXAndYFlagSelectUnitController.setSelectedCivilian(2,4);
        assertEquals(3, UnitStateController.unitSetupRanged());

        NonCivilian unit3 = new NonCivilian(tile2,civilization, UnitType.CANON);
        tile2.setNonCivilian(unit3);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,4);
        assertEquals(0, UnitStateController.unitSetupRanged());

    }

    @Test
    void unitFoundCity() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.getCivilizations().add(civilization);
        GameController.setSelectedUnit(civilian);
        when(civilian.getCivilization()).thenReturn(civilization);
        when(civilian.getUnitType()).thenReturn(UnitType.SETTLER);
        when(civilian.getCurrentTile()).thenReturn(tile);
        Civilization.TileCondition[][] list = new Civilization.TileCondition[1][1];
        when(civilization.getTileConditions()).thenReturn(list);
        assertEquals(0,UnitStateController.unitFoundCity("nude"));
    }

    @Test
    void unitCancelMission() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.getCivilizations().add(civilization);
        GameController.setSelectedUnit(nonCivilian);
        when(nonCivilian.getCivilization()).thenReturn(civilization);
        when(nonCivilian.getState()).thenReturn(UnitState.SLEEP);
        assertEquals(0,UnitStateController.unitCancelMission());
        GameController.getCivilizations().add(civilization);
    }

    @Test
    void unitDelete() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        assertEquals(1, UnitStateController.unitDelete(null));
        GameController.getCivilizations().add(civilization);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        GameController.setMap(map);
        assertEquals(2, UnitStateController.unitDelete(unit));

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        NonCivilian unit2 = new NonCivilian(tile2,civilization, UnitType.RIFLEMAN);
        tile2.setNonCivilian(unit2);
        when(civilization.getTileConditions()).thenReturn(new Civilization.TileCondition[40][40]);
        assertEquals(0, UnitStateController.unitDelete(unit2));

        Civilian unit3 = new Civilian(tile2,civilization, UnitType.SETTLER);
        tile2.setNonCivilian(unit2);
        assertEquals(0, UnitStateController.unitDelete(unit3));
    }

    @Test
    void unitBuild() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.setSelectedUnit(null);
        assertEquals(1, UnitStateController.unitBuild(ImprovementType.PASTURE));
        GameController.getCivilizations().add(civilization);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,3);
        assertEquals(2, UnitStateController.unitBuild(ImprovementType.PASTURE));

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        NonCivilian unit2 = new NonCivilian(tile2,civilization, UnitType.RIFLEMAN);
        tile2.setNonCivilian(unit2);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,4);
        assertEquals(3, UnitStateController.unitBuild(ImprovementType.PASTURE));
        Civilian civilian = new Civilian(tile2, civilization, UnitType.WORKER);
        tile2.setCivilian(civilian);
        TileXAndYFlagSelectUnitController.setSelectedCivilian(2,4);
        assertEquals(4, UnitStateController.unitBuild(ImprovementType.PASTURE));
        tile2.setImprovement(new Improvement(ImprovementType.PASTURE,tile2));
        tile2.setContainedFeature(null);
        when(civilization.doesContainTechnology(TechnologyType.ANIMAL_HUSBANDRY)).thenReturn(1);
        assertEquals(5, UnitStateController.unitBuild(ImprovementType.PASTURE));
        tile2.getImprovement().setNeedsRepair(4);
        tile2.setCivilization(civilization);
        assertEquals(0, UnitStateController.unitBuild(ImprovementType.PASTURE));
        tile2.setContainedFeature(new Feature(FeatureType.JUNGLE));
        assertEquals(0, UnitStateController.unitBuild(ImprovementType.PASTURE));
    }

    @Test
    void doesHaveTheRequiredTechnologyToBuildImprovement() {
    }

    @Test
    void unitBuildRoad() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.setSelectedUnit(null);
        assertEquals(1, UnitStateController.unitBuildRoad());
        GameController.getCivilizations().add(civilization);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,3);
        assertEquals(2, UnitStateController.unitBuildRoad());

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        NonCivilian unit2 = new NonCivilian(tile2,civilization, UnitType.RIFLEMAN);
        tile2.setNonCivilian(unit2);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,4);
        assertEquals(3, UnitStateController.unitBuildRoad());

        Civilian civilian = new Civilian(tile2, civilization, UnitType.WORKER);
        tile2.setCivilian(civilian);
        TileXAndYFlagSelectUnitController.setSelectedCivilian(2,4);
        assertEquals(0, UnitStateController.unitBuildRoad());
        assertEquals(6, UnitStateController.unitBuildRoad());
    }

    @Test
    void unitBuildRailRoad() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.setSelectedUnit(null);
        assertEquals(1, UnitStateController.unitBuildRailRoad());
        GameController.getCivilizations().add(civilization);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,3);
        assertEquals(2, UnitStateController.unitBuildRailRoad());

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        NonCivilian unit2 = new NonCivilian(tile2,civilization, UnitType.RIFLEMAN);
        tile2.setNonCivilian(unit2);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,4);
        assertEquals(3, UnitStateController.unitBuildRailRoad());

        Civilian civilian = new Civilian(tile2, civilization, UnitType.WORKER);
        tile2.setCivilian(civilian);
        TileXAndYFlagSelectUnitController.setSelectedCivilian(2,4);
        assertEquals(4, UnitStateController.unitBuildRailRoad());
        when(civilization.doesContainTechnology(TechnologyType.RAILROAD)).thenReturn(1);
        assertEquals(0, UnitStateController.unitBuildRailRoad());
        assertEquals(6, UnitStateController.unitBuildRailRoad());
    }

    @Test
    void unitRemoveFromTile() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.setSelectedUnit(null);
        assertEquals(1, UnitStateController.unitRemoveFromTile(false));
        assertEquals(1, UnitStateController.unitRemoveFromTile(true));
        GameController.getCivilizations().add(civilization);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,3);
        assertEquals(2, UnitStateController.unitRemoveFromTile(false));
        assertEquals(2, UnitStateController.unitRemoveFromTile(true));

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        NonCivilian unit2 = new NonCivilian(tile2,civilization, UnitType.RIFLEMAN);
        tile2.setNonCivilian(unit2);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,4);
        assertEquals(3, UnitStateController.unitRemoveFromTile(false));
        Civilian civilian = new Civilian(tile2, civilization, UnitType.WORKER);
        tile2.setCivilian(civilian);
        TileXAndYFlagSelectUnitController.setSelectedCivilian(2,4);
        tile2.setContainedFeature(new Feature(FeatureType.DELTA));
        assertEquals(4, UnitStateController.unitRemoveFromTile(true));
        assertEquals(5, UnitStateController.unitRemoveFromTile(false));
        tile2.setRoad(new Improvement(ImprovementType.ROAD,tile2));
        assertEquals(0, UnitStateController.unitRemoveFromTile(false));
        tile2.setContainedFeature(new Feature(FeatureType.FOREST));
        when(civilization.getTileConditions()).thenReturn(new Civilization.TileCondition[30][30]);
        assertEquals(0, UnitStateController.unitRemoveFromTile(true));
    }

    @Test
    void unitRepair() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.setSelectedUnit(null);
        assertEquals(1, UnitStateController.unitRepair());
        GameController.getCivilizations().add(civilization);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,3);
        assertEquals(2, UnitStateController.unitRepair());

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        NonCivilian unit2 = new NonCivilian(tile2,civilization, UnitType.RIFLEMAN);
        tile2.setNonCivilian(unit2);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,4);
        assertEquals(3, UnitStateController.unitRepair());
        Civilian civilian = new Civilian(tile2, civilization, UnitType.WORKER);
        tile2.setCivilian(civilian);
        TileXAndYFlagSelectUnitController.setSelectedCivilian(2,4);
        assertEquals(4, UnitStateController.unitRepair());
        tile2.setImprovement(new Improvement(ImprovementType.PASTURE,tile2));
        assertEquals(5, UnitStateController.unitRepair());
        tile2.getImprovement().setNeedsRepair(4);
        assertEquals(0, UnitStateController.unitRepair());
    }

    @Test
    void mapShowPosition() {
        MapCommandsController.mapShowPosition(-10,-10);
        MapCommandsController.mapShowPosition(200,200);
    }

    @Test
    void mapShowCityName() {
    }

    @Test
    void mapMove() {
        MapCommandsController.mapMove(4,"r");
        MapCommandsController.mapMove(4,"l");
        MapCommandsController.mapMove(4,"u");
        MapCommandsController.mapMove(4,"d");
    }

    @Test
    void buyTile() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.getCivilizations().add(civilization);
        assertEquals(2,CityCommandsController.buyTile(-2,-2));
        when(map.getX()).thenReturn(100);
        when(map.getY()).thenReturn(100);
        GameController.setMap(map);
        assertEquals(4, CityCommandsController.buyTile(10,10));
        GameController.setSelectedCity(city);
        when(city.getCivilization()).thenReturn(GameController.getCivilizations().get(0));
        assertEquals(3, CityCommandsController.buyTile(10,10));
        when(city.isTileNeighbor(any())).thenReturn(true);
        assertEquals(1, CityCommandsController.buyTile(10,10));
        when(city.addTile(any())).thenReturn(true);
        assertEquals(0, CityCommandsController.buyTile(10,10));
    }

    @Test
    void buildWall() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        assertEquals(1, CityCommandsController.buildWall());
        when(city.getName()).thenReturn("randomName");
        GameController.getCivilizations().add(civilization);
        ArrayList<City> cities = new ArrayList<>();
        cities.add(city);
        when(civilization.getCities()).thenReturn(cities);
        TileXAndYFlagSelectUnitController.setSelectedCityByName("randomName");
        assertEquals(2, CityCommandsController.buildWall());
        when(city.getCivilization()).thenReturn(civilization);
        assertEquals(0, CityCommandsController.buildWall());
        Building wall = new Building(BuildingType.WALL);
        ArrayList<Building> walls = new ArrayList<>();
        walls.add(wall);
        when(city.getHalfProducedBuildings()).thenReturn(walls);
        assertEquals(0, CityCommandsController.buildWall());
        wall.setRemainedCost(0);
        when(city.getWall()).thenReturn(wall);
        assertEquals(3, CityCommandsController.buildWall());
    }

    @Test
    void nextTurnIfYouCan() {
        assertFalse(GameController.nextTurnIfYouCan());
        if (GameController.getUnfinishedTasks().size() > 0)
            GameController.getUnfinishedTasks().subList(0, GameController.getUnfinishedTasks().size()).clear();
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
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,3);
        assertEquals(0, CheatCommandsController.cheatUnit(2,3,UnitType.SETTLER));
        assertEquals(2, CheatCommandsController.cheatUnit(2,3,UnitType.SETTLER));
        assertEquals(2, CheatCommandsController.cheatUnit(2,3,UnitType.SPEARMAN));
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
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.getCivilizations().add(civilization);
        GameController.setSelectedCity(city);
        when(city.getCivilization()).thenReturn(civilization);
        assertEquals(6,GameController.startProducingUnit("worker"));
        when(civilization.doesContainTechnology(UnitType.SETTLER.getTechnologyRequired())).thenReturn(1);
        assertEquals(0,GameController.startProducingUnit("worker"));
        HashMap<ResourcesTypes, Integer> resourcesTypes = new HashMap<>();
        when(civilization.getResourcesAmount()).thenReturn(resourcesTypes);
        assertEquals(5,GameController.startProducingUnit("SWORDSMAN"));
        resourcesTypes.put(ResourcesTypes.IRON,2);
        when(civilization.doesContainTechnology(UnitType.SWORDSMAN.getTechnologyRequired())).thenReturn(1);
        assertEquals(0,GameController.startProducingUnit("SWORDSMAN"));
        ArrayList<Unit> u = new ArrayList<>();
        u.add(nonCivilian);
        when(nonCivilian.getUnitType()).thenReturn(UnitType.SWORDSMAN);
        when(nonCivilian.getRemainedCost()).thenReturn(3);
        when(city.getHalfProducedUnits()).thenReturn(u);
        assertEquals(0,GameController.startProducingUnit("SWORDSMAN"));

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
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.getCivilizations().add(civilization);
        GameController.setSelectedUnit(nonCivilian);
        GameController.setMap(map);
        when(map.coordinatesToTile(tile.getX(), tile.getY())).thenReturn(tile);
        when(map.getX()).thenReturn(100);
        when(map.getY()).thenReturn(100);
        when(nonCivilian.getCivilization()).thenReturn(civilization);
        when(nonCivilian.getUnitType()).thenReturn(UnitType.ARCHER);
        when(nonCivilian.move(tile,true)).thenReturn(true);
        assertEquals(5,UnitStateController.unitAttack(tile.getX(), tile.getY()));
        when(map.coordinatesToTile(tile.getX(), tile.getY()).getCivilian()).thenReturn(civilian);
        assertEquals(0,UnitStateController.unitAttack(tile.getX(), tile.getY()));
        when(nonCivilian.getUnitType()).thenReturn(UnitType.CATAPULT);
        assertEquals(7,UnitStateController.unitAttack(tile.getX(), tile.getY()));
        when(nonCivilian.getState()).thenReturn(UnitState.SETUP);
        when(nonCivilian.getFortifiedCycle()).thenReturn(1);
        when(tile.getNonCivilian()).thenReturn(nonCivilian1);
        when(nonCivilian1.getCivilization()).thenReturn(null);
        assertEquals(0,UnitStateController.unitAttack(tile.getX(), tile.getY()));
        when(tile.getCity()).thenReturn(city);
        when(city.getCivilization()).thenReturn(null);
        assertEquals(0,UnitStateController.unitAttack(tile.getX(), tile.getY()));

    }

    @Test
    void cityAttack() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.getCivilizations().add(civilization);
        GameController.setMap(map);
        GameController.setSelectedCity(city);
        when(city.getCivilization()).thenReturn(civilization);
        when(map.coordinatesToTile(tile.getX(), tile.getY())).thenReturn(tile);
        when(map.getX()).thenReturn(100);
        when(map.getY()).thenReturn(100);
        when(map.coordinatesToTile(tile.getX(), tile.getY()).getNonCivilian()).thenReturn(nonCivilian);
        when(map.isInRange(2,null,tile)).thenReturn(true);
        assertEquals(0,CityCommandsController.cityAttack(tile.getX(), tile.getY()));
    }

    @Test
    void cityDestiny() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.getCivilizations().add(civilization);
        GameController.setSelectedCity(city);
        city.takeDamage(city.getHP());
        assertEquals(0,CityCommandsController.cityDestiny(true));
        assertEquals(0,CityCommandsController.cityDestiny(false));

    }

    @Test
    void cheatScience() {
        CheatCommandsController.cheatScience(10);
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
        assertEquals(1, CheatCommandsController.cheatProduction(3));
        TileXAndYFlagSelectUnitController.setSelectedCityByName("randomName");
        assertEquals(city,GameController.getSelectedCity());
        assertEquals(2, CheatCommandsController.cheatProduction(3));
        when(city.getCivilization()).thenReturn(civilization);
        assertEquals(0, CheatCommandsController.cheatProduction(3));
    }

    @Test
    void cheatResource() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        HashMap<ResourcesTypes, Integer> resource = new HashMap<>();
        resource.put(ResourcesTypes.GEMSTONE,3);
        GameController.getCivilizations().add(civilization);
        when(civilization.getResourcesAmount()).thenReturn(resource);
        CheatCommandsController.cheatResource(4, ResourcesTypes.GEMSTONE);
    }

    @Test
    void unitPillage() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.getCivilizations().add(civilization);
        GameController.nextTurn();
        GameController.setSelectedUnit(nonCivilian);
        when(nonCivilian.getCivilization()).thenReturn(civilization);
        assertEquals(3 , UnitStateController.unitPillage());

    }

    @Test
    void skipUnitTask() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.setSelectedUnit(null);
        assertEquals(1, UnitStateController.skipUnitTask());
        GameController.getCivilizations().add(civilization);
        when(civilization.getTileConditions()).thenReturn(new Civilization.TileCondition[100][100]);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,3);
        assertEquals(2, UnitStateController.skipUnitTask());

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        NonCivilian unit2 = new NonCivilian(tile2,civilization, UnitType.RIFLEMAN);
        tile2.setNonCivilian(unit2);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,4);
        assertEquals(0, UnitStateController.unitAlert());
        tile2.setNeighbours(1,tile);
        tile.setNeighbours(2,tile2);
        assertEquals(3, UnitStateController.skipUnitTask());
        GameController.getUnfinishedTasks().add(new Tasks(tile2,TaskTypes.UNIT));
        assertEquals(0, UnitStateController.skipUnitTask());
        tile2.setCivilian(new Civilian(tile2,civilization2,UnitType.SETTLER));
        tile.setCivilian(new Civilian(tile,civilization2,UnitType.SETTLER));
        GameController.getSelectedUnit().setState(UnitState.ALERT);
        unit2.setState(UnitState.ALERT);
        assertEquals(3, UnitStateController.unitAlert());
    }

    @Test
    void cheatTechnology() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        assertEquals(1, CheatCommandsController.cheatTechnology(null));
        Civilization civilization3 = new Civilization(user, 3);
        GameController.getCivilizations().add(civilization3);
        GameController.getCivilizations().add(civilization2);
        Map map2 = new Map(GameController.getCivilizations());
        GameController.setMap(map2);
        assertEquals(0, CheatCommandsController.cheatTechnology(TechnologyType.TELEGRAPH));
        Technology technology = new Technology(TechnologyType.ELECTRICITY);
        technology.setRemainedCost(5);
        civilization3.getResearches().add(technology);
        assertEquals(2, CheatCommandsController.cheatTechnology(TechnologyType.TELEGRAPH));
        assertEquals(0, CheatCommandsController.cheatTechnology(TechnologyType.ELECTRICITY));
        technology.setRemainedCost(0);
        assertEquals(2, CheatCommandsController.cheatTechnology(TechnologyType.ELECTRICITY));
    }

    @Test
    void openMap() {
        ArrayList<Civilization> civilizations = new ArrayList<>();
        civilizations.add(civilization);
        civilizations.add(civilization2);
        Map map2 = new Map(civilizations);
        GameController.setMap(map2);
        CheatCommandsController.openMap();
    }

    @Test
    void cheatMoveIt() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.setSelectedUnit(null);
        assertEquals(1, CheatCommandsController.cheatMoveIt(2,3));
        GameController.getCivilizations().add(civilization);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        NonCivilian unit = new NonCivilian(tile,civilization2, UnitType.RIFLEMAN);
        tile.setNonCivilian(unit);
        when(map.coordinatesToTile(2,3)).thenReturn(tile);
        GameController.setMap(map);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,3);
        assertEquals(2, CheatCommandsController.cheatMoveIt(2,3));

        Tile tile2 = new Tile(TileType.DESERT, 2 ,4);
        NonCivilian unit2 = new NonCivilian(tile2,civilization, UnitType.RIFLEMAN);
        Civilian civilian = new Civilian(tile2,civilization, UnitType.SETTLER);
        tile2.setNonCivilian(unit2);
        tile2.setCivilian(civilian);
        when(map.coordinatesToTile(2,4)).thenReturn(tile2);
        TileXAndYFlagSelectUnitController.setSelectedNonCivilian(2,4);
        assertEquals(3, CheatCommandsController.cheatMoveIt(-10,-10));
        when(map.getX()).thenReturn(100);
        when(map.getY()).thenReturn(100);
        assertEquals(0, CheatCommandsController.cheatMoveIt(2,4));
        TileXAndYFlagSelectUnitController.setSelectedCivilian(2,4);
        assertEquals(0, CheatCommandsController.cheatMoveIt(2,4));
    }

    @Test
    void cheatCaptureCity() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        when(city.getName()).thenReturn("randomName");
        GameController.getCivilizations().add(civilization);
        ArrayList<City> cities = new ArrayList<>();
        cities.add(city);
        when(civilization.getCities()).thenReturn(cities);
        assertEquals(1, CheatCommandsController.cheatCaptureCity("bla bla"));
        assertEquals(0, CheatCommandsController.cheatCaptureCity("randomName"));
        when(city.getCivilization()).thenReturn(civilization);
        assertEquals(2, CheatCommandsController.cheatCaptureCity("randomName"));
    }

    @Test
    void buyUnit() {
        if (GameController.getCivilizations().size() > 0)
            GameController.getCivilizations().subList(0, GameController.getCivilizations().size()).clear();
        GameController.getCivilizations().add(civilization);
        Tile tile = new Tile(TileType.DESERT, 2 ,3);
        assertEquals(1, CityCommandsController.buyUnit("asghar",58,96));
        when(GameController.getMap().coordinatesToTile(2, 3)).thenReturn(tile);
        assertEquals(2, CityCommandsController.buyUnit("archer",2,3));
        tile.setCivilization(civilization);
        assertEquals(3, CityCommandsController.buyUnit("archer",2,3));
        when(civilian.getUnitType()).thenReturn(UnitType.SETTLER);
        tile.setCivilian(civilian);
        tile.setNonCivilian(nonCivilian);
        when(civilization.getGold()).thenReturn(10000);
        assertEquals(4, CityCommandsController.buyUnit("settler",2,3));
        assertEquals(4, CityCommandsController.buyUnit("archer",2,3));
        tile.setNonCivilian(null);
        Civilization.TileCondition[][] tileConditions = new Civilization.TileCondition[40][40];
        when(civilization.getTileConditions()).thenReturn(tileConditions);
        assertEquals(0, CityCommandsController.buyUnit("archer",2,3));
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
        CheatCommandsController.cheatRoadEverywhere();
        assertNotNull(GameController.printMap());
    }
}