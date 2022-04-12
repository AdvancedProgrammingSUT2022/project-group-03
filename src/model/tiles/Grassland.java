package model.tiles;

import model.features.Feature;
import model.resources.Resource;

public class Grassland extends Tile {
    public Grassland() {
        super();
        gold = 0;
        production = 0;
        food = 2;
        changingPercentOfStrength = -33;
        movingPrice = 1;
    }

    @Override
    boolean isFeatureValid(Feature feature){
        return false;
    }
    boolean isResourceValid(Resource resource){return false;}
}
