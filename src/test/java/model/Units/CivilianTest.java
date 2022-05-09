package model.Units;

import controller.GameController;
import model.City;
import model.Civilization;
import model.features.Feature;
import model.features.FeatureType;
import model.resources.ResourcesTypes;
import model.tiles.Tile;
import model.tiles.TileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.powermock.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CivilianTest {
    @Mock
    Civilization civilization;
    @Mock
    ResourcesTypes resourcesTypes;
    City city;
    Tile tile;
    Civilian civilian;

    @BeforeEach
    public void setUp() {
        Civilization.TileCondition[][] tileConditions = new Civilization.TileCondition[15][15];
        when(civilization.getTileConditions()).thenReturn(tileConditions);
        tile = new Tile(TileType.FLAT, 5, 5);
        GameController.getCivilizations().add(civilization);
        civilian = new Civilian(tile, civilization, UnitType.SETTLER);
    }

    @Test
    void remove() {
        tile.setContainedFeature(new Feature(FeatureType.JUNGLE));
        civilian.remove(1);
        assertEquals(6,civilian.getCurrentTile().getContainedFeature().getCyclesToFinish());
    }
  
}