package model.tiles;

import model.improvements.ImprovementType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTypeTest {

    @Test
    void randomTile() {
        TileType actual = TileType.randomTile();
        assertNotNull(actual);
    }

    @Test
    void canHaveTheImprovement() {
        boolean actual = TileType.canContainImprovement(TileType.FLAT, ImprovementType.PASTURE);
        assertTrue(actual);
        actual = TileType.canContainImprovement(TileType.OCEAN, ImprovementType.PASTURE);
        assertFalse(actual);
    }
}