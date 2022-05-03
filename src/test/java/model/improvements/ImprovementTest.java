package model.improvements;

import model.Civilization;
import model.features.Feature;
import model.features.FeatureType;
import model.tiles.Tile;
import model.tiles.TileType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class ImprovementTest {
    Improvement improvement = new Improvement(ImprovementType.FIELD, new Tile(TileType.FLAT,4,4));
    @Mock
    private Tile tile;
    @Test
    void getRemainedCost() {
        assertEquals(improvement.getRemainedCost(),6);
        when(tile.getContainedFeature()).thenReturn(new Feature(FeatureType.FOREST));
        improvement = new Improvement(ImprovementType.FARM, tile);
        assertEquals(improvement.getRemainedCost(),10);
        when(tile.getContainedFeature()).thenReturn(new Feature(FeatureType.JUNGLE));
        improvement = new Improvement(ImprovementType.FARM, tile);
        assertEquals(improvement.getRemainedCost(),13);
        when(tile.getContainedFeature()).thenReturn(new Feature(FeatureType.SWAMP));
        improvement = new Improvement(ImprovementType.FARM, tile);
        assertEquals(improvement.getRemainedCost(),12);
        improvement = new Improvement(ImprovementType.ROAD, tile);
        assertEquals(improvement.getRemainedCost(),3);
    }

    @Test
    void setRemainedCost() {
        improvement.setRemainedCost(22);
        assertEquals(improvement.getRemainedCost(),22);
    }

    @Test
    void getNeedsRepair() {
        improvement.setNeedsRepair(4);
        assertEquals(improvement.getNeedsRepair(),4);
    }

    @Test
    void setNeedsRepair() {
        improvement.setNeedsRepair(4);
        assertEquals(improvement.getNeedsRepair(),4);
    }

    @Test
    void getImprovementType() {
        assertEquals(improvement.getImprovementType(),ImprovementType.FIELD);
    }
}