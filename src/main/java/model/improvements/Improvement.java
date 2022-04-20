package model.improvements;

import model.technologies.TechnologyType;
import model.tiles.TileType;

import java.util.HashMap;

public class Improvement {
    private static HashMap<ImprovementType, TechnologyType> prerequisitesTechnologies;
    private static HashMap<ImprovementType, TileType> possibleTiles;
    private static HashMap<ImprovementType, Integer> food;
    private static HashMap<ImprovementType, Integer> production;
    private static HashMap<ImprovementType, Integer> gold;
    private ImprovementType improvementType;
    private boolean needsRepair;

    static {

    }
    public Improvement(ImprovementType improvementType)
    {

    }


}
