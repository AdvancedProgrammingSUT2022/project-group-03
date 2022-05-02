package model.Units;

import model.City;
import model.Civilization;
import model.tiles.Tile;

public class Civilian extends Unit {
    private static int cost;
    public Civilian(Tile tile, Civilization civilization, UnitType unitType) {
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
