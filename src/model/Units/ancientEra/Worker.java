package model.Units.ancientEra;

import model.Civilization;
import model.Units.Civilian;
import model.Units.Unit;
import model.improvements.ImprovementType;
import model.tiles.Tile;

public class Worker extends Civilian {
    private static int cost;

    int remainingDurationToBuildCompletely;

    public Worker(Tile tile, Civilization civilization) {
        super(tile, civilization);
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
