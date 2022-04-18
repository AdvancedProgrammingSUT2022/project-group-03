package model.resources;

import model.improvements.ImprovementType;
import model.technologies.Technology;
import model.technologies.TechnologyType;

import java.util.ArrayList;
import java.util.HashMap;

public class Resource {
    private static HashMap<ResourcesTypes,TechnologyType> necessaryTechnologies;
    private static HashMap<Resource, Integer> food;
    private static HashMap<Resource, Integer> production;
    private static HashMap<Resource, Integer> gold;
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

}
