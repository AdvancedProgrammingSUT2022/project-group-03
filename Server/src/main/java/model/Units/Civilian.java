package model.Units;

import controller.gameController.GameController;
import controller.gameController.TileXAndYFlagSelectUnitController;
import model.City;
import model.Civilization;
import model.tiles.Tile;

public class Civilian extends Unit {
    public Civilian(Tile tile, Civilization civilization, UnitType unitType) {
        super(tile, civilization, unitType);
    }

    public void city(String string,GameController gameController,TileXAndYFlagSelectUnitController tileXAndYFlagSelectUnitController) {
        City tempCity = new City(this.currentTile, string, civilization,gameController);
        civilization.getCities().add(tempCity);
        currentTile.setCity(tempCity);
        tileXAndYFlagSelectUnitController.setSelectedCityByPosition(currentTile.getX(), currentTile.getY());
        gameController.setUnfinishedTasks();
    }

    public void remove(int isJungle,GameController gameController) {
        if (isJungle == 1) {
            if (currentTile.getContainedFeature().getCyclesToFinish() == -1)
                currentTile.getContainedFeature().setCyclesToFinish(6);
            gameController.openNewArea(currentTile, civilization, null);
            state = UnitState.REMOVING;
        }
    }
}
