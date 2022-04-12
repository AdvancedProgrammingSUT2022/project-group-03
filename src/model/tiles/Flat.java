package model.tiles;

import model.features.Feature;
import model.resources.Resource;

public class Flat extends Tile{
    public Flat(){
        super();
        gold = 0;
        production = 1;
        food = 1;
        changingPercentOfStrength = -33;
        movingPrice = 1;
    }
    boolean isFeatureValid(Feature feature){
        return false;
    }
    boolean isResourceValid(Resource resource){return false;}
}
