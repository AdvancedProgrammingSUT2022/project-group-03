package model.Units;

import model.resources.Resource;
import model.technologies.Technology;
import model.tiles.Tile;

import java.util.ArrayList;

public class RangedUnit extends NonCivilian{
    private int range;
    private int rangedCombatStrength;

    public RangedUnit(Tile tile, ArrayList<Resource> resources, ArrayList<Technology> technologies, int civilizationGold) {
        super(tile, resources, technologies, civilizationGold);
    }

    protected boolean isReady()
    {
        return true;
    }
    private void attack(Tile tile) {
    }
    private boolean defense(Tile tile)
    {
        return true;
    }

}
