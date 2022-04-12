package model.resources;

import model.technologies.Technology;

import java.util.ArrayList;

public class Resource {
    protected static int gold;
    protected static int food;
    protected static int production;
    protected static ArrayList<Technology> necessaryTechnologies;

    public static int getFood() {
        return food;
    }

    public static int getProduction() {
        return production;
    }

    public static int getGold() {
        return gold;
    }

    public static ArrayList<Technology> getNecessaryTechnologies() {
        return necessaryTechnologies;
    }
}
