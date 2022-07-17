package com.example.demo.model.building;

import com.example.demo.model.Producible;

public class Building implements Producible {
    BuildingType buildingType;
    int remainedCost;

    public Building(BuildingType buildingType) {
        this.buildingType = buildingType;
        remainedCost = buildingType.getCost();
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
}
