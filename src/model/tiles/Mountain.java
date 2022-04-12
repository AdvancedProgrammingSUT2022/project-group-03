package model.tiles;

import model.features.Feature;
import model.resources.Resource;

public class Mountain extends Tile {
    public Mountain() {
        super();
        gold = 0;
        production = 0;
        food = 0;
        changingPercentOfStrength = 0;
        movingPrice = 12345678;
    }

    @Override
    boolean isFeatureValid(Feature feature){
        return false;
    }
    boolean isResourceValid(Resource resource){return false;}
}
