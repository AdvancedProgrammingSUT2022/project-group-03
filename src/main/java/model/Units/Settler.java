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
        this.unitType = UnitType.SETTLER;
    }
    public void city()
    {
        City tempCity = new City(this.currentTile , "KazemLand",civilization);
        civilization.getCities().add(tempCity);
        currentTile.setCity(tempCity);
    }

}
