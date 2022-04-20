package model;

import model.Units.Unit;
import model.resources.ResourcesTypes;
import model.technologies.Technology;
import model.technologies.TechnologyType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Civilization {
    private final String username;
    private int[][] openedArea = null;
    private int gold;
    private ArrayList<Unit> units = new ArrayList<>();
    private int science;
    private int happiness;
    public Civilization(String username)
    {
        this.username= username;
        this.gold = 0;
    }



    public ArrayList<Unit> getUnits() {
        return units;
    }


    public String getUsername() {
        return username;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void setOpenedArea(int x, int y) {
        this.openedArea = new int[x][y];
    }

    public int[][] getOpenedArea() {
        return openedArea;
    }

    public int getGold() {
        return gold;
    }

    public void SetTheNumbers()
    {

    }

    public void deleteUnit(Unit unit)
    {

    }

    private void countTheTotalOfCityResources()
    {

    }
}
