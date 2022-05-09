package model.Units;

import model.City;
import model.Civilization;
import model.User;
import model.improvements.Improvement;
import model.improvements.ImprovementType;
import model.tiles.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NonCivilianTest {
    @Mock
    User user;
    @Mock
    Tile tile,tile2;
    @Mock
    City city;
    @Mock
    Improvement improvement;
    @Mock
    Civilization civilization, civilization2;
    NonCivilian nonCivilian;

    @Test
    void attack() {
        nonCivilian = new NonCivilian(tile,civilization,UnitType.ARCHER);
        when(tile2.getCity()).thenReturn(city);
        when(tile.getCombatChange()).thenReturn(10);
        when(civilization.getHappiness()).thenReturn(10);
        when(civilization.getTileConditions()).thenReturn(new Civilization.TileCondition[20][20]);
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