package model.tiles;

import model.features.Feature;
import model.resources.Resource;

public class Snow extends Tile{
    public Snow(){
        super();
        gold = 0;
        production = 0;
        food = 0;
        changingPercentOfStrength = -33;
        movingPrice = 1;
    }

    @Override
    boolean isFeatureValid(Feature feature){
        return false;
    }
    boolean isResourceValid(Resource resource){return false;}
}
