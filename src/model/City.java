package model;

import model.Units.Unit;
import model.tiles.Tile;

import java.util.ArrayList;

public class City {
    private final String NAME;
    private int strength;
    private final Tile maintile;
    private Civilization civilization;
    private boolean doesHaveWall;
    private int HP = 20;
    private ArrayList<Tile> Tiles;
    public City(Tile tile) {
        this.maintile = tile;

    }

    public void setTheNumbers() {

    }

    public boolean buildWall() {
        return false;
    }

    public boolean defense(Unit attackers)
    {

    }

    public void attack(Tile tile)
    {

    }
}
