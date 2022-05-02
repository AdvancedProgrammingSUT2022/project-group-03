package model.Units;

import controller.GameController;
import model.Civilization;
import model.improvements.Improvement;
import model.improvements.ImprovementType;
import model.tiles.Tile;

public class Worker extends Unit {
    public Worker(Tile tile, Civilization civilization, UnitType unitType) {
        super(tile, civilization,unitType);
    }

    public void repair()
    {
        state = UnitState.REPAIRING;
    }
    public void buildImprovement(ImprovementType type)
    {
        currentTile.setImprovement(new Improvement(type,getCurrentTile()));
        state=UnitState.BUILDING;
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
