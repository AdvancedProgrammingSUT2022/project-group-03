package model.Units;

import model.Civilization;
import model.resources.Resource;
import model.technologies.Technology;
import model.tiles.Tile;

import java.util.ArrayList;

public class Siege extends RangedUnit{
    private Tile AimedTile;

    public Siege(Tile tile, Civilization civilization) {
        super(tile, civilization);
    }

    @Override
    protected boolean isReady()
    {
        return AimedTile!=null;
    }

    private void attack(Tile tile) {
    }
}
