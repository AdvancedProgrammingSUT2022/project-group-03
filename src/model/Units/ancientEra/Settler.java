package model.Units.ancientEra;

import model.City;
import model.Civilization;
import model.Units.Civilian;
import model.Units.Unit;
import model.tiles.Tile;

public class Settler extends Civilian {
    private static int cost;

    public Settler(Tile tile, Civilization civilization) {
        super(tile, civilization);
    }

    public City city(Tile tile)
    {
        return null;
    }
}
