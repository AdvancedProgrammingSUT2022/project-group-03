package model.technologies;

import java.util.ArrayList;
import java.util.HashMap;

public class Technology {
    private static HashMap<TechnologyType, Integer> cost;
    private static HashMap<TechnologyType, TechnologyType[]> nextTechs;
    private TechnologyType technologyType;
    private int remainedScienceUntilCompleteTechnology;
    static {

    }
    public Technology(TechnologyType technologyType){

    }
    public static int getCost(TechnologyType technologyType) {
        return cost.get(technologyType);
    }
}
