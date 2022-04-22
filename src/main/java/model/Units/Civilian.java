package model.Units;

import model.Civilization;
import model.resources.Resource;
import model.technologies.Technology;
import model.tiles.Tile;

import java.util.ArrayList;

public class Civilian extends Unit{
    public Civilian(Tile tile, Civilization civilization)
    {
        super(tile,civilization);
    }
    public static boolean canBeMade(Civilization civilization, int civilizationGold) {
        return false;
    }
    public String getIcon() {
        return null;
    }
}
