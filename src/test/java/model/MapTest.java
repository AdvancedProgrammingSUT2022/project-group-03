package model;

import controller.gameController.GameController;
import model.Units.NonCivilian;
import model.Units.UnitType;
import model.features.Feature;
import model.features.FeatureType;
import model.tiles.TileType;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;

import static controller.gameController.GameController.openNewArea;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MapTest {

    @Test
    void addStartingSettlers() {
    }

    @Test
    void coordinatesToTile() {
    }

    @Test
    void isInRange() {
    }

    @Test
    void getX() {
    }

    @Test
    void getY() {
    }
    @Mock
    User i;
    Civilization civilization;
    @Test
    void findNextTile() {
        civilization = new Civilization(i,1);
        Civilization civilization1 = new Civilization(i,1);
        ArrayList<Civilization> arrayList= new ArrayList<>();
        arrayList.add(civilization);
        arrayList.add(civilization1);
        Map map = new Map(arrayList);
        map.getTiles()[8][1].setTileType(TileType.GRASSLAND);
        map.getTiles()[8][1].setContainedFeature(null);
        map.getTiles()[7][1].setTileType(TileType.TUNDRA);
        map.getTiles()[7][1].setContainedFeature(null);
        map.getTiles()[6][1].setTileType(TileType.GRASSLAND);
        map.getTiles()[6][1].setContainedFeature(null);
        map.getTiles()[8][2].setTileType(TileType.TUNDRA);
        map.getTiles()[8][2].setContainedFeature(new Feature(FeatureType.FOREST));
        NonCivilian unit = new NonCivilian(map.getTiles()[8][1],civilization, UnitType.PANZER);
        map.getTiles()[8][1].setNonCivilian(unit);
        civilization.setTileConditions
                (new Civilization.TileCondition[map.getX()][map.getY()]);
        for(int i = 0;i<map.getX();i++)
            for(int j=0;j<map.getY();j+=2)
                openNewArea(map.coordinatesToTile(i,j),civilization,null);
        try (MockedStatic<GameController> utilities = Mockito.mockStatic(GameController.class)) {
            utilities.when(GameController::getMap).thenReturn(map);
            unit.move(map.getTiles()[6][1],true);

        }
        int x = 4;
        x++;


    }

    @Test
    void printMap() {
    }
}