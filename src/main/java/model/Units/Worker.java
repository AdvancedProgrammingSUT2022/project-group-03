package model.Units;

import model.Civilization;
import model.improvements.ImprovementType;
import model.tiles.Tile;

public class Worker extends Unit {
    private static int cost;
    int remainingDurationToBuildCompletely;
    public Worker(Tile tile, Civilization civilization, UnitType unitType) {
        super(tile, civilization,unitType);
    }

    public boolean repair()
    {
        return true;
    }
    public void buildImprovement(ImprovementType type)
    {

    }
    public void buildRoad(int isRailRoad)
    {

    }
    public int remove(int isJungle)
    {
        return 0;
    }


}
