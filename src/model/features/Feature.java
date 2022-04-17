package model.features;

import model.improvements.ImprovementType;
import model.tiles.TileType;

import java.util.HashMap;

public class Feature {
    private static HashMap<FeatureType, Integer> movingPrice;
    private static HashMap<FeatureType, Integer> food;
    private static HashMap<FeatureType, Integer> production;
    private static HashMap<FeatureType, Integer> gold;
    private static HashMap<FeatureType, Integer> changingPercentOfStrength;
    private static HashMap<FeatureType, Integer> possibleTiles;
    private FeatureType featureType;
    public Feature(FeatureType featureType)
    {

    }
    public int getGold() {
        return gold;
    }
    public int getProduction() {
        return production;
    }
    public int getFood() {
        return food;
    }
    public int getChangingPercentOfStrength() {
        return changingPercentOfStrength;
    }
    public int getMovingPrice() {
        return movingPrice;
    }
}
