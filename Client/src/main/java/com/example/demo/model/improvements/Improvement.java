package com.example.demo.model.improvements;

import com.example.demo.model.features.FeatureType;
import com.example.demo.model.tiles.Tile;

import java.io.Serial;
import java.io.Serializable;


public class Improvement implements Serializable {
    @Serial
    private static final long serialVersionUID = 8994185412231419955L;
    private final ImprovementType improvementType;
    private int needsRepair;
    int remainedCost;

    public Improvement(ImprovementType improvementType, Tile tile) {
        this.improvementType = improvementType;
        needsRepair = 0;
        remainedCost = 6;
        if ((improvementType == ImprovementType.FARM || improvementType == ImprovementType.MINE) && tile.getContainedFeature() != null) {
            if (tile.getContainedFeature().getFeatureType() == FeatureType.FOREST)
                remainedCost = 10;
            if (tile.getContainedFeature().getFeatureType() == FeatureType.JUNGLE)
                remainedCost = 13;
            if (tile.getContainedFeature().getFeatureType() == FeatureType.SWAMP)
                remainedCost = 12;
        }
        if (improvementType == ImprovementType.ROAD || improvementType == ImprovementType.RAILROAD)
            remainedCost = 3;
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
