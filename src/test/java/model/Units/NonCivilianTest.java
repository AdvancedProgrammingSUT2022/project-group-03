package model.Units;

import model.City;
import model.Civilization;
import model.User;
import model.improvements.Improvement;
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
    User user;
    @Mock
    Tile tile;
    @Mock
    City city;
    @Mock
    Improvement improvement;
    @Mock
    Civilization civilization;
    NonCivilian nonCivilian = new NonCivilian(tile,civilization,UnitType.ARCHER);

    @Test
    void getUnitType() {
    }

    @Test
    void attack() {
    }

    @Test
    void calculateDamage() {
    }

    @Test
    void pillage() {
        nonCivilian.currentTile = tile;
        assertFalse(nonCivilian.pillage());
    }

    @Test
    void getFortifiedCycle() {
    }

    @Test
    void setFortifiedCycle() {
    }
}