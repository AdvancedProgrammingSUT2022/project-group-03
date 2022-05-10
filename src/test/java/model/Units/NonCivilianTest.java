package model.Units;

import model.City;
import model.Civilization;
import model.User;
import model.improvements.Improvement;
import model.improvements.ImprovementType;
import model.tiles.Tile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NonCivilianTest {
    @Mock
    Tile tile,tile2;
    @Mock
    City city;
    @Mock
    Civilization civilization, civilization2;
    @Mock
    User user;
    NonCivilian nonCivilian,nonCivilian2;

    @Test
    void attack() {
        nonCivilian = new NonCivilian(tile,civilization,UnitType.PIKEMAN);
        nonCivilian2 = new NonCivilian(tile2,civilization,UnitType.HORSEMAN);
        when(tile2.getNonCivilian()).thenReturn(nonCivilian2);
        when(civilization.getTileConditions()).thenReturn(new Civilization.TileCondition[20][20]);
        when(civilization.getUser()).thenReturn(user);
        when(user.getNickname()).thenReturn("I've got some tricks up my sleeve");
        when(civilization.getUser()).thenReturn(user);
        when(user.getNickname()).thenReturn("lord save me");
        nonCivilian.attack(tile2);
        nonCivilian = new NonCivilian(tile,civilization,UnitType.ANTI_TANK_GUN);
        nonCivilian2 = new NonCivilian(tile2,civilization,UnitType.TANK);
        when(tile2.getNonCivilian()).thenReturn(nonCivilian2);
        nonCivilian.attack(tile2);
        nonCivilian = new NonCivilian(tile,civilization,UnitType.CATAPULT);
        when(tile2.getCity()).thenReturn(city);
        when(civilization.getHappiness()).thenReturn(10);
        nonCivilian.attack(tile2);
        nonCivilian = new NonCivilian(tile,civilization,UnitType.TANK);
        nonCivilian.attack(tile2);
    }

    @Test
    void calculateDamage() {
        nonCivilian = new NonCivilian(tile,civilization,UnitType.RIFLEMAN);
        assertEquals(32,nonCivilian.calculateDamage(1.2));
        assertEquals(27,nonCivilian.calculateDamage(0.9));
    }

    @Test
    void pillage() {
        nonCivilian = new NonCivilian(tile,civilization,UnitType.ARCHER);
        assertFalse(nonCivilian.pillage());
        when(tile.getCivilization()).thenReturn(civilization2);
        Improvement improvement = new Improvement(ImprovementType.PASTURE,tile);
        improvement.setRemainedCost(0);
        when(tile.getImprovement()).thenReturn(improvement);
        assertTrue(nonCivilian.pillage());

        improvement = new Improvement(ImprovementType.PASTURE,tile);
        improvement.setRemainedCost(0);
        when(tile.getImprovement()).thenReturn(improvement);
        Improvement road = new Improvement(ImprovementType.ROAD,tile);
        road.setRemainedCost(0);
        when(tile.getRoad()).thenReturn(road);
        assertTrue(nonCivilian.pillage());
    }

    @Test
    void getFortifiedCycle() {
    }

    @Test
    void setFortifiedCycle() {
    }
}