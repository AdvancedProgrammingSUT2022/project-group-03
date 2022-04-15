package model.technologies;

import java.util.ArrayList;
import java.util.HashMap;

public class Technology {
    private static int cost;
    private static ArrayList<Technology> nextTechs;
    private int remainedCyclesUntilCompleteTechnology;
    private static HashMap<TechnologyType, TechnologyType[]> nextTypes;
    static {

    }
    public static int getCost() {
        return cost;
    }
}
