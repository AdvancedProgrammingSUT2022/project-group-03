package com.example.demo.model.Units;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.controller.gameController.TileXAndYFlagSelectUnitController;
import com.example.demo.model.City;
import com.example.demo.model.Civilization;
import com.example.demo.model.tiles.Tile;

import java.io.Serial;

public class Civilian extends Unit {
    @Serial
    private static final long serialVersionUID = 7777138419195060495L;
    public Civilian(Tile tile, Civilization civilization, UnitType unitType) {
        super(tile, civilization, unitType);
    }

    public void city(String string) {
        City tempCity = new City(this.currentTile, string, civilization);
        civilization.getCities().add(tempCity);
        if(civilization.getMainCapital()==null)
        {
            civilization.setMainCapital(tempCity);
            tempCity.setMainCapital(true);
        }
        currentTile.setCity(tempCity);
        TileXAndYFlagSelectUnitController.setSelectedCityByPosition(currentTile.getX(), currentTile.getY());
        GameController.setUnfinishedTasks();
    }

    public void remove(int isJungle) {
        if (isJungle == 1) {
            if (currentTile.getContainedFeature().getCyclesToFinish() == -1)
                currentTile.getContainedFeature().setCyclesToFinish(6);
            GameController.openNewArea(currentTile, civilization, null);
            state = UnitState.REMOVING;
        }
    }

    @Override
    public double greenBarPercent() {
        return (double) health/100;
    }

    @Override
    public double blueBarPercent() {
        return (double) movementPrice/unitType.movePoint;
    }

    @Override
    public String getHealthDigit() {
        return health + "/100";
    }

    @Override
    public Tile getTile() {
        return currentTile;
    }


}
