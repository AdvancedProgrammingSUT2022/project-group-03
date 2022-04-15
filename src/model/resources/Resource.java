package model.resources;

import model.technologies.Technology;
import model.technologies.TechnologyType;

import java.util.ArrayList;

public class Resource {
    private int gold;
    private int food;
    private int production;
    private ArrayList<TechnologyType> necessaryTechnologies;
    private ResourcesTypes resourcesTypes;
    private ResourcesCategory resourcesCategory;
    public Resource(ResourcesTypes resourcesTypes)
    {

    }
    public int getFood() {
        return food;
    }

    public int getProduction() {
        return production;
    }

    public int getGold() {
        return gold;
    }

    public ArrayList<TechnologyType> getNecessaryTechnologies() {
        return necessaryTechnologies;
    }
}
