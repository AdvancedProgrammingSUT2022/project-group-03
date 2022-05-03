package model.resources;

import model.Civilization;
import model.Units.Unit;
import model.User;
import model.features.FeatureType;
import model.technologies.Technology;
import model.technologies.TechnologyType;
import model.tiles.Tile;
import model.tiles.TileType;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
class ResourcesTypesTest {
    @Test
    void randomResources() {
        assertNotNull(ResourcesTypes.randomResource());
    }

    @Test
    void stringToEnum()
    {
        assertSame(ResourcesTypes.GEMSTONE, ResourcesTypes.stringToEnum("gemSTone"));
        assertSame(ResourcesTypes.WHEAT, ResourcesTypes.stringToEnum("WHEAT"));
        assertSame(ResourcesTypes.GEMSTONE, ResourcesTypes.stringToEnum("gemstone"));
    }

    @Mock
    User user;


    @Test
    void isTechnologyUnlocked(){

        //User user = Mockito.mock(User.class);
        //when(user.getNickname()).thenReturn("technologies");
        /*ArrayList<Technology> technologies = new ArrayList<>();
        when(civilization.getResearches()).thenReturn(technologies);
        ResourcesTypes resourcesTypes = ResourcesTypes.IRON;
        assertFalse(resourcesTypes.isTechnologyUnlocked(civilization,new Tile(TileType.randomTile(),0,0)));
        //technologies.add(new Technology())*/

    }



}