package model.building;

import model.Producible;

public class Building implements Producible {
    BuildingType buildingType;
    int remainedCost;
    public Building(BuildingType buildingType)
    {
        this.buildingType = buildingType;
        remainedCost = buildingType.cost;
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
        return buildingType.cost;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }
}
