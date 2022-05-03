package model.building;

public enum BuildingType {
    WALL(80);
    public final int cost;
    BuildingType(int cost)
    {
        this.cost= cost;
    }
}
