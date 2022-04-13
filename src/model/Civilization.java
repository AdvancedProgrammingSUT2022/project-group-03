package model;

import model.Units.Unit;
import model.technologies.Technology;

import java.util.ArrayList;

public class Civilization {
    private final String username;
    private int[][] openedArea = null;
    private int gold;
    private ArrayList<Unit> units = new ArrayList<>();
    private ArrayList<Technology> researches;
    private ArrayList<City> cities;
    public Civilization(String username)
    {
        this.username= username;
        this.gold = 0;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public ArrayList<Technology> getResearches() {
        return researches;
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
}
