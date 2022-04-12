package model;

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
}

