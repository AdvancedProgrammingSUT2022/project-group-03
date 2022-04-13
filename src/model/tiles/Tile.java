package model.tiles;

import model.City;
import model.Units.Civilian;
import model.Units.NonCivilian;
import model.Units.Unit;
import model.features.Feature;
import model.resources.Resource;

abstract public class Tile {
    protected static int movingPrice;
    protected static int food;
    protected static int production;
    protected static int gold;
    protected static int changingPercentOfStrength;
    protected Feature feature;
    protected static int x;
    protected static int y;
    protected int[] resources;
    protected Civilian civilian;
    protected NonCivilian nonCivilian;
    protected building;
    protected City city;
    protected int hasRoad;
    //0==no road
    //1==road
    //2= railroad
    //3==both
    protected final Tile[] NEIGHBOURS = new Tile[6];

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
        return feature;
    }

    public int[] getResources() {
        return resources;
    }


    public boolean setFeature(Feature feature){
        if(isFeatureValid(feature)){
            this.feature = feature;
            return true;
        }
        return false;
    }
    abstract boolean isFeatureValid(Feature feature);

    public boolean setResource(Resource resource){
        if(isResourceValid(resource)){
            return true;
        }
        return false;
    }
    abstract boolean isResourceValid(Resource resource);
}
