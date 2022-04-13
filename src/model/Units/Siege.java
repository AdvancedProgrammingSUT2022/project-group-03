package model.Units;

import model.resources.Resource;
import model.technologies.Technology;
import model.tiles.Tile;

import java.util.ArrayList;

public class Siege extends RangedUnit{
    private Tile AimedTile;

    public Siege(Tile tile, ArrayList<Resource> resources, ArrayList<Technology> technologies, int civilizationGold) {
        super(tile, resources, technologies, civilizationGold);
    }

    @Override
    protected boolean isReady()
    {
        return AimedTile!=null;
    }

    private void attack(Tile tile) {
    }
}
