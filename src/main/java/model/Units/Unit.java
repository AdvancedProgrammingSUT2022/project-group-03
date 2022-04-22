package model.Units;

import controller.GameController;
import model.Civilization;
import model.features.FeatureType;
import model.productable;
import model.tiles.Tile;
import model.tiles.TileType;

public class Unit implements productable {
    protected Civilization civilization;
    private static int state;
    protected Tile currentTile;
    private Tile destinationTile;
    private int defaultMovementPrice;
    private int health = 10;
    private int XP;
    private boolean hasDoneAnything;

    public Unit(Tile tile, Civilization civilization) {
        this.currentTile = tile;
        this.civilization = civilization;
    }

    public int getHealth() {
        return health;
    }


    public int getDefaultMovementPrice() {
        return defaultMovementPrice;
    }



    public Civilization getCivilization() {
        return civilization;
    }

    @Override
    public void getCost(){

    }

    public void cancelMission()
    {

    }

    public void wake()
    {

    }
    public void startTheTurn() {
        openNewArea();
//        health += ...
        if(health>10)
            health=10;
//        movementPrice =



    }

    public void endTheTurn()
    {
        if(destinationTile!=null)
            move(destinationTile);
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    private void openNewArea() {
        for(int i =0 ;i <6;i++)
        {
            int neighbourX = currentTile.getNeighbours(i).getX();
            int neighbourY = currentTile.getNeighbours(i).getY();
            civilization.tileConditions[neighbourX][neighbourY]  = new Civilization.TileCondition(currentTile.getNeighbours(i).CloneTile(), true);
            if(currentTile.getNeighbours(i).getTileType()== TileType.MOUNTAIN ||
                    currentTile.getNeighbours(i).getTileType()== TileType.HILL ||
                    (currentTile.getNeighbours(i).getFeature()!= null && (currentTile.getNeighbours(i).getFeature().getFeatureType() == FeatureType.FOREST ||
                    currentTile.getNeighbours(i).getFeature().getFeatureType() == FeatureType.DENSEFOREST)))
                continue;
            for(int j=0;j<6;j++)
            {
                neighbourX = currentTile.getNeighbours(i).getNeighbours(j).getX();
                neighbourY = currentTile.getNeighbours(i).getNeighbours(j).getY();
                civilization.tileConditions[neighbourX][neighbourY]  = new Civilization.TileCondition(currentTile.getNeighbours(i).getNeighbours(j).CloneTile(), true);
            }
        }
        civilization.tileConditions[currentTile.getX()][currentTile.getY()] = new Civilization.TileCondition(currentTile.CloneTile(), true);
    }


    public boolean move(Tile destinationTile) {
        int movementPrice;
        if(this instanceof Civilian)
            movementPrice=((Civilian) this).MOVEMENTPRICE;
        else
            movementPrice= ((NonCivilian) this).getUnitType().movePoint;
        Tile[] tiles = GameController.getMap().findNextTile(currentTile,movementPrice,destinationTile,this instanceof Civilian);
        this.destinationTile= destinationTile;

        if(tiles==null)
        {
            this.destinationTile=null;
            return false;
        }
        if(tiles.length==1)
            this.destinationTile = null;
        Tile tempTile = null;
        for(int i =tiles.length-1; i>=0; i--)
            if(tiles[i]!=null)
            {
                tempTile=tiles[i];
                break;
            }
        if(tempTile==null)
            return false;
        if(this instanceof NonCivilian)
        {
            this.currentTile.setNonCivilian(null);
            tempTile.setNonCivilian((NonCivilian) this);
        }
        else
        {
            this.currentTile.setCivilian(null);
            tempTile.setCivilian((Civilian) this);
        }
        this.currentTile = tempTile;
        openNewArea();
        return true;
    }
}
