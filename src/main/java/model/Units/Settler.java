package model.Units;

import model.City;
import model.Civilization;
import model.tiles.Tile;

public class Settler extends Unit {
    private static int cost;
    public Settler(Tile tile, Civilization civilization, UnitType unitType) {
        super(tile, civilization, unitType);
    }
    {
        this.unitType = UnitType.Settler;
    }
    public void city()
    {
        civilization.getCities().add(new City(this.currentTile , "Random name",civilization));
    }

}
