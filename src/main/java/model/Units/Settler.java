package model.Units;

import model.City;
import model.Civilization;
import model.Color;
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

    @Override
    public String getIcon() {
        return Color.getColorByNumber(civilization.getColor())+"S"+ Color.RESET +" ";
    }
}
