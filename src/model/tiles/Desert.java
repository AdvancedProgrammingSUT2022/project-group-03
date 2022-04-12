package model.tiles;

import model.features.Feature;
import model.resources.Resource;

public class Desert extends Tile{
    {
        resources = new int[]{
                -1,// banana
                -1,// cow
                -1,// deer
                0, // sheep
                -1,// wheat

                // luxe:
                0,  // cotton
                -1, // color
                -1, // fur
                0,  // gemstone
                0,  // gold
                0,  // bokhoor
                -1, // tusk
                0,  // marble
                -1, // silk
                0,  // silver
                -1, // sugar

                // strategic
                -1, // coal
                -1, // horse
                0 // iron
        };
    }
    public Desert(){
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
