package model.Units;

import model.Civilization;
import model.Map;
import model.productable;
import model.resources.Resource;
import model.technologies.Technology;
import model.technologies.TechnologyType;
import model.tiles.Tile;

import java.util.ArrayList;
import java.util.HashMap;

public class Unit implements productable {
    private Tile currentTile;
    private Tile destinationTile;
    private static Civilization civilization;
    private int defaultMovementPrice;
    private int movementPrice;
    private static int combatStrength;
    private static Resource resource;
    private static int state;
    private int health = 10;
    private int XP;
    private boolean hasDoneAnything;

    public int getHealth() {
        return health;
    }

    private void OpenNewArea() {


    }

    private void move(Tile tile) {
        Tile tempTile = Map.FindBestMove(this);


    }

    public int getDefaultMovementPrice() {
        return defaultMovementPrice;
    }

    public void setMovementPrice(int movementPrice) {
        this.movementPrice = movementPrice;
    }

    public int getMovementPrice() {
        return movementPrice;
    }



    public Unit(Tile tile, Civilization civilization) {

    }

    public void setTheNumbers() {

    }

    public static Civilization getCivilization() {
        return civilization;
    }

    public void cancelMission()
    {

    }

    public void wake()
    {

    }

    public static Unit stringToUnit(String string)
    {
        return null;
    }

    @Override
    public void getCost(){

    }
}
