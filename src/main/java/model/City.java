package model;

import model.Units.Civilian;
import model.Units.NonCivilian;
import model.Units.NonCivilianUnitType;
import model.Units.Unit;
import model.tiles.Tile;

import java.util.ArrayList;

public class City{
    private final String name;
    private int remainingProduction;
    private int strength;
    private final Tile mainTile;
    private Civilization civilization;
    private boolean doesHaveWall;
    private int HP = 20;
    private int food;
    private int population;
    private int production;
    private ArrayList<Tile> tiles = new ArrayList<>();
    private Civilization founder;
    private int citizen;
    private ArrayList<Tile> gettingWorkedOnByCitizensTiles;
    public City(Tile tile,String name) {
        this.mainTile = tile;
        this.name = name;
        this.tiles.add(mainTile);
        for(int i = 0; i < 6; i++)
            this.tiles.add(mainTile.getNeighbours(i));
    }

    public void startTheTurn() {

    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    private void countTheTotalOfCityResources()
    {

    }
    public boolean buildWall() {
        return false;
    }

    public boolean defense(Unit attackers)
    {
        return false;
    }

    public void attack(Tile tile)
    {

    }

    public void buyTile(Tile tile)
    {

    }

    public void assignCitizenToTiles(Tile originTile,Tile destinationTile)
    {

    }

//    public static NonCivilian canCreateUnit(Tile tile, Civilization civilization, NonCivilianUnitType unitType)
//    {
//        if(unitType.get(unitType)>civilization.getGold())
//            return null;
//        return new NonCivilian(tile,civilization,unitType);
//    }

}
