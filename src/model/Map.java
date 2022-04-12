package model;

import model.Units.Unit;
import model.tiles.Tile;

import java.util.ArrayList;

public class Map {
    ArrayList<Tile> tiles;
    private int x;
    private int y;
    public Map(ArrayList<Civilization> civilizations)
    {
        GenerateMap(civilizations);
        for (Civilization civilization : civilizations) civilization.setOpenedArea(x, y);
    }

    private void GenerateMap(ArrayList<Civilization> civilizations)
    {

    }
    public Tile coordinatesToTile(int x, int y)
    {
        return null;
    }
    public static Tile FindBestMove(Unit unit)
    {
        return null;
    }
    public boolean isTileValid(Unit unit)
    {

    }
    public boolean isRangeValid(Unit unit, Tile tile)
    {

    }
}

