package model.tiles;

import model.City;
import model.Civilization;
import model.Units.*;
import model.features.Feature;
import model.features.FeatureType;
import model.improvements.Improvement;
import model.resources.Resource;
import model.resources.ResourcesTypes;

import java.util.ArrayList;
import java.util.HashMap;

public class Tile {
    private static HashMap<TileType,ResourcesTypes[]> possibleResourceTypes;
    private boolean[] tilesWithRiver = new boolean[6];
    private TileType tileType;
    private Resource containedResource;
    private Feature containedFeature;
    private Improvement improvement;
    private final int x;
    private final int y;
    private Civilization civilization;
    private Unit civilian;
    private NonCivilian nonCivilian;
    private City city;
    private int hasRoad;
    private int raidLevel;
    static {
        //hashmap set
    }
    private final Tile[] neighbours = new Tile[6];// L , clockwise

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public City getCity() {
        return city;
    }

    public int getMovingPrice() {
        if(containedFeature!=null)
            return tileType.movementPoint + containedFeature.getMovingPrice();
        return tileType.movementPoint;
    }

    public Unit getCivilian() {
        return civilian;
    }

    public NonCivilian getNonCivilian() {
        return nonCivilian;
    }

    public Civilization getCivilization() {
        return civilization;
    }

    public Tile getNeighbours(int i) {
        if(i >= 0 && i < 6){
            return neighbours[i];
        }
        return null;
    }
    public void setTilesWithRiver(int i){
        if(i >= 0 && i < 6){
            tilesWithRiver[i] = true;
        }
    }

    public boolean isRiverWithNeighbour(int i ){
        if(i >= 0 && i < 6){
            return tilesWithRiver[i];
        }
        return false;
    }

    public void setNeighbours(int i, Tile tile) {
        neighbours[i] = tile;
    }

    public TileType getTileType() {
        return tileType;
    }
    public Feature getFeature() {
        return containedFeature;
    }

    public Resource getResources() {
        return containedResource;
    }

    public Tile(TileType tileType,int x, int y){
        this.x = x;
        this.y = y;
        this.tileType = tileType;
    }
    public boolean setFeature(Feature feature){
        this.containedFeature = feature;
        return true;
    }



    public boolean isFeatureTypeValid(FeatureType featureType){
        FeatureType[] list = tileType.featureTypes;
        for (FeatureType validFeatureType : list) {
            if (validFeatureType == featureType) {
                if(featureType == FeatureType.DELTA) {
                    for (int i = 0; i < 6; i++) {
                        if(tilesWithRiver[i]) return true;
                    }
                    return false;
                }
                return true;
            }
        }
        return false;
    }
    public boolean setResource(Resource resource){
//        if(isResourceValid(resource)){
//            return true;
//        }
        return false;
    }

    public void setCivilian(Unit unit) {
        if(unit.getUnitType().combatType== CombatType.CIVILIAN)
            this.civilian = unit;
    }

    public void setNonCivilian(NonCivilian nonCivilian) {
        this.nonCivilian = nonCivilian;
    }

    public Tile CloneTile()
    {
        Tile newTile = new Tile(this.tileType,this.x,this.y);
        newTile.tilesWithRiver = this.tilesWithRiver.clone();
        newTile.containedResource = this.containedResource;
        newTile.containedFeature = this.containedFeature;
        newTile.improvement = this.improvement;
        newTile.civilization = this.civilization;
        newTile.civilian = this.civilian;
        newTile.nonCivilian = this.nonCivilian;
        newTile.city = this.city;
        newTile.hasRoad = this.hasRoad;
        newTile.raidLevel = this.raidLevel;
        return newTile;
    }
}
