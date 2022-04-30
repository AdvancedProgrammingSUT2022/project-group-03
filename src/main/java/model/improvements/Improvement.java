package model.improvements;

import model.technologies.TechnologyType;
import model.tiles.TileType;

import java.util.HashMap;

public class Improvement {
    private ImprovementType improvementType;
    private int needsRepair;
    int remainedCost;
    public Improvement(ImprovementType improvementType)
    {
        this.improvementType = improvementType;
        needsRepair = 0;
        remainedCost=6;
    }

    public int getRemainedCost() {
        return remainedCost;
    }

    public void setRemainedCost(int remainedCost) {
        this.remainedCost = remainedCost;
    }

    public int getNeedsRepair() {
        return needsRepair;
    }

    public void setNeedsRepair(int needsRepair) {
        this.needsRepair = needsRepair;
    }

    public ImprovementType getImprovementType() {
        return improvementType;
    }
}
