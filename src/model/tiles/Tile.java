package model.tiles;

import model.City;
import model.Civilization;
import model.Units.Civilian;
import model.Units.NonCivilian;
import model.features.Feature;
import model.features.FeatureType;
import model.improvements.Improvement;
import model.resources.Resource;
import model.resources.ResourcesTypes;

import java.util.HashMap;

public class Tile {
    private static int movingPrice;
    private static int food;
    private static int production;
    private static int gold;
    private static int changingPercentOfStrength;
    private TileType tileType;
    private static int x;
    private static int y;
    private static HashMap<TileType,ResourcesTypes[]> possibleResourceTypes;
    private ResourcesTypes containedResource;
    private static HashMap<TileType, FeatureType[]> possibleFeatureTypes;
    private Feature containedFeature;
    private Civilization civilization;
    private Civilian civilian;
    private NonCivilian nonCivilian;
    private City city;
    private int hasRoad;
    private boolean hasBeenRaid;
    private Improvement improvement;
    //0==no road
    //1==road
    //2= railroad
    //3==both
    static {
        //hashmap set
    }
    private final Tile[] NEIGHBOURS = new Tile[6];

    public static int getX() {
        return x;
    }

    public static int getY() {
        return y;
    }

    public static int getMovingPrice() {
        return movingPrice;
    }

    public static int getChangingPercentOfStrength() {
        return changingPercentOfStrength;
    }

    public static int getFood() {
        return food;
    }

    public static int getProduction() {
        return production;
    }

    public static int getGold() {
        return gold;
    }

    public Feature getFeature() {
        return containedFeature;
    }

    public ResourcesTypes getResources() {
        return containedResource;
    }


    public boolean setFeature(Feature feature){
        if(isFeatureValid(feature)){
            this.containedFeature = feature;
            return true;
        }
        return false;
    }
    boolean isFeatureValid(Feature feature){
        return false;
    }

    public boolean setResource(Resource resource){
        if(isResourceValid(resource)){
            return true;
        }
        return false;
    }
    boolean isResourceValid(Resource resource){
        return false;
    }
}
