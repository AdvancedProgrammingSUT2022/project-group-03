package com.example.demo.model.building;

import com.example.demo.model.Producible;
import com.example.demo.model.tiles.Tile;
import com.example.demo.view.HealthyBeing;

import java.io.Serial;
import java.io.Serializable;

public class Building implements Serializable, Producible/*, HealthyBeing*/ {
    @Serial
    private static final long serialVersionUID = 6194185412231414725L;
    private final BuildingType buildingType;
    private final Tile tile;
    int remainedCost;

    public Building(BuildingType buildingType,Tile tile) {
        this.buildingType = buildingType;
        remainedCost = buildingType.getCost();
        this.tile=tile;
        tile.setBuilding(this);
    }

    @Override
    public String getName() {
        return buildingType.toString();
    }

    @Override
    public int getRemainedCost() {
        return remainedCost;
    }

    @Override
    public void setRemainedCost(int remainedCost) {
        this.remainedCost = remainedCost;
    }

    @Override
    public int getCost() {
        return buildingType.getCost();
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }
//
//    @Override
//    public double greenBarPercent() {
//        return 0;
//    }
//
//    @Override
//    public double blueBarPercent() {
//        return 0;
//    }
//
//    @Override
//    public String getHealthDigit() {
//        return null;
//    }

    public Tile getTile() {
        return tile;
    }
}
