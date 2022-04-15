package model.improvements;

import model.Units.NonCivilianUnitType;
import model.technologies.Technology;
import model.technologies.TechnologyType;
import model.tiles.TileType;

import java.util.HashMap;

public class Improvement {
    private static HashMap<ImprovementType, TechnologyType> prerequisitesTechnologies;
    private int food;
    private int production;
    private int gold;
    private boolean needsRepair;
    private static HashMap<ImprovementType, TileType> possibleTiles;

    static {

    }

    public Improvement(ImprovementType improvementType)
    {

    }
}
