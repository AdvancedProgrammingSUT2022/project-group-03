package model.tiles;

import model.features.Feature;
import model.resources.Resource;

public class Hill extends Tile {
    public Hill() {
        super();
        gold = 0;
        production = 2;
        food = 0;
        changingPercentOfStrength = 25;
        movingPrice = 2;
    }

    @Override
    boolean isFeatureValid(Feature feature){
        return false;
    }
    boolean isResourceValid(Resource resource){return false;}

}
