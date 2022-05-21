package model;

import controller.gameController.GameController;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityTest {
    @Mock
    Civilization civilization;
    @Mock
    Civilization anotherCivilization;
    @Mock
    ResourcesTypes resourcesTypes;
    @Mock
    User user;

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
        city.getGettingWorkedOnByCitizensTiles().add(tile);
        assertEquals(city.collectFood(), 1);
        tile.setImprovement(new Improvement(ImprovementType.MINE, tile));
        assertEquals(city.collectFood(), 1);
        tile.setContainedFeature(new Feature(FeatureType.JUNGLE));
        assertEquals(city.collectFood(), 2);
        when(resourcesTypes.isTechnologyUnlocked(any(), any())).thenReturn(true);
        when(resourcesTypes.getImprovementType()).thenReturn(ImprovementType.MINE);
        tile.setResource(resourcesTypes);
        assertEquals(city.collectFood(), 2);
        when(resourcesTypes.getFood()).thenReturn(500);
        city.getHalfProducedUnits().add(civilian);
        city.startTheTurn();
    }

    @Test
    void collectResources() {
        Civilization civilization2 = new Civilization(new User("z","z","z"),2);
        Civilization.TileCondition[][] tileCondition = new Civilization.TileCondition[100][100];
        civilization2.setTileConditions(tileCondition);
        City city2 = new City(tile,"sss", civilization2);
        tile.setImprovement(new Improvement(ImprovementType.FARM,tile));
        civilization2.getResearches().add(new Technology(TechnologyType.AGRICULTURE));
        civilization2.getResearches().get(0).changeRemainedCost(0);
        tile.setContainedFeature(new Feature(FeatureType.OASIS));
        ResourcesTypes resource = ResourcesTypes.WHEAT;
        tile.setResource(resource);
        city2.getGettingWorkedOnByCitizensTiles().add(tile);
        city2.getCivilization().getResourcesAmount().put(resource,1);
        assertTrue(civilization2.getResourcesAmount().get(resource)!=0);
        city2.collectResources(city2.getCivilization().getResourcesAmount());
        assertTrue(civilization2.getResourcesAmount().get(resource)!=0);
    }

    @Test
    void collectProduction() {
        Civilization civilization2 = new Civilization(new User("z","z","z"),2);
        Civilization.TileCondition[][] tileCondition = new Civilization.TileCondition[100][100];
        civilization2.setTileConditions(tileCondition);
        City city2 = new City(tile,"sss", civilization2);
        tile.setImprovement(new Improvement(ImprovementType.FARM,tile));
        civilization2.getResearches().add(new Technology(TechnologyType.AGRICULTURE));
        civilization2.getResearches().get(0).changeRemainedCost(0);
        tile.setContainedFeature(new Feature(FeatureType.OASIS));
        ResourcesTypes resource = ResourcesTypes.WHEAT;
        tile.setResource(resource);
        assertEquals(city2.collectProduction(),1);
    }

    @Test
    void mines5Percent(){
//        city.min
    }

    @Test
    void properTileForProduct(){

    }

    @Test
    void startTheTurn() {
        tile.setNeighbours(2,anotherTile);
        city.startTheTurn();
        city.setProduct(civilian);
        city.startTheTurn();
        city.getProduct().setRemainedCost(0);
        city.startTheTurn();
        NonCivilian nonCivilian = new NonCivilian(tile,civilization,UnitType.TANK);
        NonCivilian nonCivilian2 = new NonCivilian(tile,civilization,UnitType.TANK);
        city.setProduct(nonCivilian);
        city.getProduct().setRemainedCost(0);
        city.startTheTurn();

        city.setProduct(nonCivilian2);
        city.getProduct().setRemainedCost(0);
        city.startTheTurn();

        city.setProduct(civilian);
        city.getProduct().setRemainedCost(0);
        city.startTheTurn();

        Tile thirdTile = new Tile(TileType.FLAT, 6, 6);
        anotherTile.setNeighbours(4,thirdTile);

        city.setProduct(nonCivilian2);
        city.getProduct().setRemainedCost(0);
        city.startTheTurn();

        city.setProduct(civilian);
        city.getProduct().setRemainedCost(0);
        city.startTheTurn();

        city.setProduct(civilian);
        city.getProduct().setRemainedCost(0);
        city.startTheTurn();

        Building building = new Building(BuildingType.WALL);
        city.setProduct(building);
        city.getProduct().setRemainedCost(0);
        city.startTheTurn();
        city.getTiles().get(0).setCivilian(new Civilian(null,null,UnitType.SETTLER));
        assertEquals(0,city.getAnxiety());
        city.startTheTurn();

    }

    @Test
    void expandBorders() {
        city.expandBorders();
        assertEquals(city.getTiles().size(),1);
    }

    @Test
    void getGold() {
        Civilization civilization2 = new Civilization(new User("z","z","z"),2);
        Civilization.TileCondition[][] tileCondition = new Civilization.TileCondition[100][100];
        civilization2.setTileConditions(tileCondition);
        City city2 = new City(tile,"sss", civilization2);
        tile.setImprovement(new Improvement(ImprovementType.FARM,tile));
        civilization2.getResearches().add(new Technology(TechnologyType.AGRICULTURE));
        civilization2.getResearches().get(0).changeRemainedCost(0);
        tile.setContainedFeature(new Feature(FeatureType.OASIS));
        ResourcesTypes resource = ResourcesTypes.WHEAT;
        tile.setResource(resource);
        city2.getGettingWorkedOnByCitizensTiles().add(tile);
        assertEquals(city2.getGold(),1);
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
        assertEquals(city.getCitizen(),1);
    }

    @Test
    void attack() {
        NonCivilian nonCivilian = new NonCivilian(tile,civilization,UnitType.TANK);
        city.setProduct(nonCivilian);
        city.getProduct().setRemainedCost(0);
        city.startTheTurn();
        when(civilization.getUser()).thenReturn(user);
        when(user.getNickname()).thenReturn("love you too the moon and to saturn");
        city.attack(tile);
    }

    @Test
    void isTileNeighbor() {
        assertFalse(city.isTileNeighbor(anotherTile));
        tile.setNeighbours(2, anotherTile);
        city = new City(tile, "randomBS", civilization);
        assertTrue(city.isTileNeighbor(anotherTile));
    }

    @Test
    void assignCitizenToTiles() {
        assertTrue(city.assignCitizenToTiles(null,tile));
        assertFalse(city.assignCitizenToTiles(anotherTile,tile));
        city.getTiles().add(anotherTile);
        assertFalse(city.assignCitizenToTiles(anotherTile,tile));
        city.getGettingWorkedOnByCitizensTiles().add(anotherTile);
        assertTrue(city.assignCitizenToTiles(anotherTile,tile));
    }

    @Test
    void doWeHaveEnoughMoney() {
        assertFalse(city.doWeHaveEnoughMoney(UnitType.TANK));
        when(civilization.getGold()).thenReturn(1000);
        assertTrue(city.doWeHaveEnoughMoney(UnitType.SETTLER));
    }

    @Test
    void getCombatStrength() {
        NonCivilian nonCivilian = new NonCivilian(tile,city.getCivilization(),UnitType.TANK);
        nonCivilian.setState(UnitState.GARRISON);
        tile.setNonCivilian(nonCivilian);
        assertEquals(city.getCombatStrength(true),54);
        nonCivilian = new NonCivilian(tile,city.getCivilization(),UnitType.ARCHER);
        nonCivilian.setState(UnitState.GARRISON);
        tile.setNonCivilian(nonCivilian);
        assertEquals(10,city.getCombatStrength(true));
        tile = new Tile(TileType.HILL, 5, 5);
        city = new City(tile, "randomBS", civilization);
        assertEquals(6,city.getCombatStrength(false));
    }

    @Test
    void checkToDestroy() {
        assertFalse(city.checkToDestroy());
        when(civilization.getUser()).thenReturn(user);
        when(user.getNickname()).thenReturn("I've got some tricks up my sleeve");
        city.takeDamage(city.getHP(),civilization);
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
        when(civilization.getUser()).thenReturn(user);
        when(user.getNickname()).thenReturn("is this the end of all the endings?");
        city.destroy(civilization);
        assertNull(tile.getCity());
    }

    @Test
    void changeCivilization() {
        Civilization.TileCondition[][] list = new Civilization.TileCondition[10][10];
        list[tile.getX()][tile.getY()] = new Civilization.TileCondition(tile,true);
        when(anotherCivilization.getTileConditions()).thenReturn(list);
        city.getHalfProducedUnits().add(civilian);
        when(anotherCivilization.getUser()).thenReturn(user);
        when(user.getNickname()).thenReturn("nice to meet you,where you been");
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
        when(civilization.getUser()).thenReturn(user);
        when(user.getNickname()).thenReturn("stronger than a 90's trend");
        city.takeDamage(2,civilization);
        assertEquals(city.getHP(), hp - 2);
    }

    @Test
    void cyclesToComplete() {
        assertEquals(3, city.cyclesToComplete(3));
        city.getGettingWorkedOnByCitizensTiles().add(tile);
        city.getGettingWorkedOnByCitizensTiles().remove(0);
        city.getMainTile().setContainedFeature(null);
        city.getMainTile().setImprovement(null);
        city.getMainTile().setResource(null);
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