package model;

import model.Units.Unit;
import model.tiles.Tile;

import java.util.ArrayList;

public class Map {

    private ArrayList<Tile> tiles;
    private int x;
    private int y;
    public static Tile FindBestMove(Unit unit)
    {
        return null;
    }
    public Map(ArrayList<Civilization> civilizations)
    {
        GenerateMap(civilizations);
        for (Civilization civilization : civilizations) civilization.setOpenedArea(x, y);
    }


    public Tile coordinatesToTile(int x, int y)
    {
        return null;
    }
    public boolean isTileValid(Unit unit)
    {

    }
    public boolean isRangeValid(Unit unit, Tile tile)
    {

    }
    private void GenerateMap(ArrayList<Civilization> civilizations)
    {

    }

    public boolean doTheseHaveRiver(Tile firstTile, Tile secondTile)
    {

    }
}

