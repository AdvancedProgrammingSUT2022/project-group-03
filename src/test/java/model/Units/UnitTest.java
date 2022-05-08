package model.Units;

import model.building.controller.GameController;
import model.Civilization;
import model.Map;
import model.features.Feature;
import model.features.FeatureType;
import model.improvements.Improvement;
import model.improvements.ImprovementType;
import model.tiles.Tile;
import model.tiles.TileType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UnitTest {
    @Mock
    Civilization civilization;
    @Mock
    Map map;
    Tile tile = new Tile(TileType.FLAT,0,0);
    NonCivilian nonCivilian = new NonCivilian(tile,civilization,UnitType.ARCHER);
    Civilian civilian = new Civilian(tile,civilization,UnitType.SETTLER);
    @Mock
    Map.TileAndMP tileAndMP;
    @Test
    void getCombatStrength() {
        when(civilization.getHappiness()).thenReturn(1);
        civilian.civilization = civilization;
        assertEquals(civilian.getCombatStrength(true),1);
        assertEquals(civilian.getCombatStrength(false),1);
        NonCivilian nonCivilian = new NonCivilian(tile,civilization,UnitType.ARCHER);
        nonCivilian.setState(UnitState.FORTIFY);
        assertTrue(Math.abs(nonCivilian.getCombatStrength(false) - 2.5)< 0.5);
        nonCivilian.cancelMission();
        nonCivilian = new NonCivilian(tile,civilization,UnitType.CHARIOT_ARCHER);
        tile.setContainedFeature(new Feature(FeatureType.JUNGLE));
        assertTrue(Math.abs(nonCivilian.getCombatStrength(false) - 2.5)< 0.5);
        when(civilization.getHappiness()).thenReturn(-1);
        tile.setContainedFeature(new Feature(FeatureType.FOREST));
        assertTrue(Math.abs(nonCivilian.getCombatStrength(false) - 2.5)< 0.5);
        tile.setContainedFeature(new Feature(FeatureType.ICE));
        assertTrue(Math.abs(nonCivilian.getCombatStrength(false) - 2.5)< 0.5);

    }

    @Test
    void checkToDestroy(){
        NonCivilian nonCivilian = new NonCivilian(tile,civilization,UnitType.ARCHER);
        nonCivilian.health = -1;
        assertTrue(nonCivilian.checkToDestroy());
        nonCivilian.health = 1;
        assertFalse(nonCivilian.checkToDestroy());
        civilian.health =-1;
        civilian.civilization = civilization;
        assertTrue(civilian.checkToDestroy());
        assertEquals(civilian.getHealth(),-1);
        assertEquals(civilian.getCivilization(),civilization);
        assertEquals(civilian.getCost(),UnitType.SETTLER.cost);


    }

    @Test
    void getDestinationTile() {
        assertNull(civilian.getDestinationTile());
    }

    @Test
    void getCurrentTile() {
        civilian.setCurrentTile(tile);
        assertEquals(tile,civilian.getCurrentTile());
    }

    @Test
    void move() {
        nonCivilian.movementPrice = 0;
        assertFalse(nonCivilian.move(tile,true));
        nonCivilian.state = UnitState.ATTACK;
        try (MockedStatic<GameController> utilities = Mockito.mockStatic(GameController.class)) {
            utilities.when(GameController::getMap).thenReturn(map);
            when(map.isInRange(2,tile,tile)).thenReturn(true);
            nonCivilian.movementPrice = 1;
            nonCivilian.currentTile = tile;
            nonCivilian.destinationTile = tile;
            nonCivilian.endTheTurn();
            assertEquals(1, nonCivilian.movementPrice);
        }
        nonCivilian.state = UnitState.AWAKE;
        try (MockedStatic<GameController> utilities = Mockito.mockStatic(GameController.class)) {
            utilities.when(GameController::getMap).thenReturn(map);
            nonCivilian.civilization = null;
            when(map.findNextTile(null,tile,1,UnitType.ARCHER.movePoint,tile,false,nonCivilian)).thenReturn(null);
            nonCivilian.movementPrice = 1;
            nonCivilian.currentTile = tile;
            nonCivilian.destinationTile = tile;
            assertFalse(nonCivilian.move(tile,true));
        }
        try (MockedStatic<GameController> utilities = Mockito.mockStatic(GameController.class)) {
            utilities.when(GameController::getMap).thenReturn(map);
            nonCivilian.civilization = null;
            Map.TileAndMP[] tileAndMP = new Map.TileAndMP[1];
            tileAndMP[0] =new Map.TileAndMP(0,tile);
            when(map.findNextTile(null,tile,1,UnitType.ARCHER.movePoint,tile,false,nonCivilian)).thenReturn(tileAndMP);
            nonCivilian.movementPrice = 1;
            nonCivilian.currentTile = tile;
            nonCivilian.destinationTile = tile;
            assertTrue(nonCivilian.move(tile,true));
            nonCivilian.movementPrice = 1;
            tile.setNonCivilian(nonCivilian);
            assertFalse(nonCivilian.move(tile,true));
            nonCivilian.movementPrice = 1;
            tile.setNonCivilian(null);
            tileAndMP[0].movePoint =2;
            assertTrue(nonCivilian.move(tile,true));
            when(map.findNextTile(null,tile,1,UnitType.SETTLER.movePoint,tile,true,civilian)).thenReturn(tileAndMP);
            civilian.civilization = null;
            civilian.movementPrice = 1;
            assertTrue(civilian.move(tile,true));
            tileAndMP = new Map.TileAndMP[2];
            tileAndMP[0] = new Map.TileAndMP(0,tile);
            Tile tile1 = new Tile(TileType.FLAT,0,0);
            tileAndMP[1] = new Map.TileAndMP(1,tile1);
            civilian.movementPrice =3;
            tile.setCivilian(null);
            when(map.findNextTile(null,tile,3,UnitType.SETTLER.movePoint,tile1,true,civilian)).thenReturn(tileAndMP);
            assertTrue(civilian.move(tile1,false));
        }


    }

    @Test
    void getMovementPrice() {
        assertEquals(civilian.getMovementPrice(),2);
    }

    @Test
    void takeDamage() {
        int health = civilian.health;
        civilian.takeDamage(3);
        assertEquals(health-3,civilian.getHealth());
    }

    @Test
    void getState() {
        civilian.setState(UnitState.AWAKE);
        assertEquals(civilian.getState(),UnitState.AWAKE);
    }

    @Test
    void getRemainedCost() {
        civilian.setRemainedCost(10);
        assertEquals(civilian.getRemainedCost(),10);
    }

    @Test
    void getUnitType() {
        assertEquals(civilian.getUnitType(),UnitType.SETTLER);
    }

    @Test
    void startTheTurn(){
        try (MockedStatic<GameController> utilities = Mockito.mockStatic(GameController.class)) {
            utilities.when(() -> GameController.openNewArea(tile, civilization,nonCivilian)).thenReturn(true);
            nonCivilian.health = 10;
            nonCivilian.startTheTurn();
            assertEquals(20,nonCivilian.getHealth());
            nonCivilian.health = 100;
            nonCivilian.setState(UnitState.FORTIFY);
            nonCivilian.startTheTurn();
            assertEquals(100,nonCivilian.getHealth());
            Civilian civilian = new Civilian(tile,civilization,UnitType.WORKER);
            civilian.state = UnitState.BUILDING;
            tile.setImprovement(new Improvement(ImprovementType.FARM,tile));
            civilian.startTheTurn();
            assertEquals(5,tile.getImprovement().getRemainedCost());
            tile.getImprovement().setRemainedCost(1);
            civilian.startTheTurn();
            assertEquals(0,tile.getImprovement().getRemainedCost());
            tile.getImprovement().setNeedsRepair(1);
            civilian.state = UnitState.REPAIRING;
            civilian.startTheTurn();
            assertEquals(0,tile.getImprovement().getNeedsRepair());
            civilian.state = UnitState.REPAIRING;
            civilian.startTheTurn();
            assertEquals(0,tile.getImprovement().getNeedsRepair());
            civilian.state = UnitState.REMOVING;
            civilian.startTheTurn();
            assertEquals(0,tile.getImprovement().getNeedsRepair());
            civilian.getCurrentTile().setContainedFeature(new Feature(FeatureType.OASIS));
            civilian.getCurrentTile().getContainedFeature().setCyclesToFinish(1);
            civilian.state = UnitState.REMOVING;
            civilian.startTheTurn();
            assertNull(civilian.getCurrentTile().getContainedFeature());
        }
    }

    @Test
    void getName()
    {
        assertEquals(civilian.getName(),"SETTLER");
    }
}