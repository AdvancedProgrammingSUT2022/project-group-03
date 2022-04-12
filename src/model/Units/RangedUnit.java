package model.Units;

import model.resources.Resource;
import model.technologies.Technology;
import model.tiles.Tile;

import java.util.ArrayList;

public class RangedUnit extends NonCivilian{
    private int range;
    private int rangedCombatStrength;
    public RangedUnit(int x, int y, ArrayList<Resource> resources, ArrayList<Technology> technologies, int civilizationGold)
    {
        super();

    }
    private boolean attack(Tile tile) {
        return true;
    }
    private boolean defense()
    {
        return true;
    }

}
