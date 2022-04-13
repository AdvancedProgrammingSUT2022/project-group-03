package model.Units;

import model.Civilization;
import model.resources.Resource;
import model.technologies.Technology;
import model.tiles.Tile;

import java.util.ArrayList;

public class Civilian extends Unit{
    public Civilian(Tile tile, ArrayList<Resource> resources, ArrayList<Technology> technologies, Civilization civilization)
    {
        super(tile,civilization);
    }
    public boolean repairing()
    {
        return true;
    }
}
