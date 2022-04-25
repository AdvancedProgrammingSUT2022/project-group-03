package model.improvements;

import model.technologies.TechnologyType;
import model.tiles.TileType;

import java.util.HashMap;

public class Improvement {
    private ImprovementType improvementType;
    private boolean needsRepair;
    public Improvement(ImprovementType improvementType)
    {
        this.improvementType = improvementType;
        needsRepair = false;
    }


}
