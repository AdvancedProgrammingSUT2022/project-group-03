package model.resources;

import model.Civilization;

import java.util.ArrayList;
import java.util.Locale;


import model.improvements.ImprovementType;
import model.technologies.Technology;
import model.technologies.TechnologyType;
import model.tiles.Tile;
import model.tiles.TileType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class ResourcesTypesTest {


    @Test
    void randomResource() {
        Assertions.assertNotNull(ResourcesTypes.randomResource());

    }

    @Mock private Civilization civilization;

    @Test
    void isTechnologyUnlocked() {

        ArrayList<Technology> technologies = new ArrayList<>();
        when(civilization.getResearches()).thenReturn(technologies);
        ResourcesTypes resourcesTypes = ResourcesTypes.IRON;
        Assertions.assertFalse(resourcesTypes.isTechnologyUnlocked(civilization,new Tile(TileType.randomTile(),0,0)));
        technologies.add(new Technology(TechnologyType.CALENDAR));
        when(civilization.doesContainTechnology(TechnologyType.CALENDAR)).thenReturn(1);
        Assertions.assertTrue(ResourcesTypes.BANANA.isTechnologyUnlocked(civilization,new Tile(TileType.randomTile(),0,0)));
        Assertions.assertFalse(ResourcesTypes.IRON.isTechnologyUnlocked(civilization,new Tile(TileType.randomTile(),0,0)));
        technologies.add(new Technology(TechnologyType.IRON_WORKING));
        when(civilization.doesContainTechnology(TechnologyType.MINING)).thenReturn(1);
        Assertions.assertTrue(ResourcesTypes.IRON.isTechnologyUnlocked(civilization,new Tile(TileType.randomTile(),0,0)));


    }

    @Test
    void stringToEnum() {
        Assertions.assertSame(ResourcesTypes.GEMSTONE, ResourcesTypes.stringToEnum("gemSTone"));
        Assertions.assertSame(ResourcesTypes.WHEAT, ResourcesTypes.stringToEnum("WHEAT"));
        Assertions.assertSame(ResourcesTypes.GEMSTONE, ResourcesTypes.stringToEnum("gemstone"));
        Assertions.assertNull(ResourcesTypes.stringToEnum("gemstoneiii"));
    }

    @Test
    void values() {
        Assertions.assertSame(ResourcesTypes.VALUES.get(0) ,ResourcesTypes.BANANA);
    }

    @Test
    void valueOf() {
        Assertions.assertSame(ResourcesTypes.valueOf("BANANA") , ResourcesTypes.BANANA);
    }

    @Test
    void getImprovementType() {
        assertEquals(ResourcesTypes.FUR.getImprovementType(), ImprovementType.CAMP);
    }
}