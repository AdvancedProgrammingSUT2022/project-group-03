package model.Units;

import controller.GameController;
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
