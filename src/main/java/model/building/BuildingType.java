package model.building;

public enum BuildingType {
    WALL(80);
    public int cost;
    BuildingType(int cost)
    {
        this.cost= cost;
    }
}
