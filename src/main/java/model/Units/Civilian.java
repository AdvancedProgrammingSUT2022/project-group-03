package model.Units;

import controller.GameController;
import model.City;
import model.Civilization;
import model.tiles.Tile;

public class Civilian extends Unit {
    public Civilian(Tile tile, Civilization civilization, UnitType unitType) {
        super(tile, civilization, unitType);
    }
    public void city(String string)
    {
        City tempCity = new City(this.currentTile , string,civilization);
        civilization.getCities().add(tempCity);
        currentTile.setCity(tempCity);
        GameController.setSelectedCityByPosition(currentTile.getX(),currentTile.getY());
        GameController.setUnfinishedTasks();
    }
    public void remove(int isJungle)
    {
        if(isJungle==1)
        {
            if(currentTile.getContainedFeature().getCyclesToFinish()==-1)
                currentTile.getContainedFeature().setCyclesToFinish(6);
            GameController.openNewArea(currentTile,civilization,null);
            state = UnitState.REMOVING;
        }
    }
}
