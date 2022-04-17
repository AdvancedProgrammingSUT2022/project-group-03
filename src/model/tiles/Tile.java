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

import java.util.ArrayList;
import java.util.HashMap;

public class Tile {
    private static HashMap<TileType,Integer> movingPrice;
    private static HashMap<TileType,Integer> food;
    private static HashMap<TileType,Integer> production;
    private static HashMap<TileType,Integer> gold;
    private static HashMap<TileType,Integer> changingPercentOfStrength;
    private static HashMap<TileType,ResourcesTypes[]> possibleResourceTypes;
    private static HashMap<TileType, FeatureType[]> possibleFeatureTypes;
    private ArrayList<Tile> tilesWithRiver;
    private TileType tileType;
    private Resource containedResource;
    private Feature containedFeature;
    private Improvement improvement;
    private final int x;
    private final int y;
    private Civilization civilization;
    private Civilian civilian;
    private NonCivilian nonCivilian;
    private City city;
    private int hasRoad;
    private int raidLevel;
    //0==no road
    //1==road
    //2= railroad
    //3==both
    static {
        //hashmap set
    }
    private final Tile[] NEIGHBOURS = new Tile[6];

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getMovingPrice() {
        return movingPrice;
    }

    public int getChangingPercentOfStrength() {
        return changingPercentOfStrength;
    }

    public int getFood() {
        return food;
    }

    public int getProduction() {
        return production;
    }

    public int getGold() {
        return gold;
    }

    public Feature getFeature() {
        return containedFeature;
    }

    public Resource getResources() {
        return containedResource;
    }

    public Tile(TileType tileType,int x, int y){

    }
    public boolean setFeature(Feature feature){
        if(isFeatureValid(feature)){
            this.containedFeature = feature;
            return true;
        }
        return false;
    }
    public boolean setResource(Resource resource){
        if(isResourceValid(resource)){
            return true;
        }
        return false;
    }

}
