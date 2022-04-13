package model.Units;

import model.Civilization;
import model.Map;
import model.resources.Resource;
import model.technologies.Technology;
import model.tiles.Tile;

import java.util.ArrayList;

public class Unit {
    private Tile currentTile;
    private Tile destinationTile;
    private static Civilization civilization;
    private static int cost;
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

    public static boolean canBeMade(ArrayList<Resource> resources, ArrayList<Technology> technologies, int civilizationGold) {
        return false;
    }

    public Unit(Tile tile, ArrayList<Resource> resources, ArrayList<Technology> technologies, int civilizationGold) {

    }

    public void setTheNumbers() {

    }

    public static Civilization getCivilization() {
        return civilization;
    }
}
