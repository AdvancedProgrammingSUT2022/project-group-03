package model.Units;

import controller.GameController;
import model.Civilization;
import model.Map;
import model.tiles.Tile;
import model.tiles.TileType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
    void endTheTurn() {
        when(civilization.getHappiness()).thenReturn(1);
        civilian.civilization = civilization;
        assertEquals(civilian.getCombatStrength(true),1);
        assertEquals(civilian.getCombatStrength(false),1);

    }

    @Test
    void getDestinationTile() {
    }

    @Test
    void getCurrentTile() {
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
            //when(map.findNextTile(null,tile,1,UnitType.SETTLER.movePoint,tile,true,civilian)).thenReturn(tileAndMP);
            assertTrue(civilian.move(tile1,false));
        }


    }

    @Test
    void getMovementPrice() {
    }

    @Test
    void attack() {
    }

    @Test
    void takeDamage() {
    }

    @Test
    void setState() {
    }

    @Test
    void getState() {
    }

    @Test
    void getRemainedCost() {
    }

    @Test
    void setRemainedCost() {
    }

    @Test
    void getUnitType() {
    }

    @Test
    void buildRoad() {
    }

    @Test
    void setCurrentTile() {
    }
}