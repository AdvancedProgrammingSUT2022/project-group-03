package model.Units;

import controller.GameController;
import model.Civilization;
import model.features.FeatureType;
import model.productable;
import model.tiles.Tile;
import model.tiles.TileType;

public class Unit implements productable {
    private Civilization civilization;
    private static int state;
    private Tile currentTile;
    private Tile destinationTile;
    private int defaultMovementPrice;
    private int movementPrice;
    private int health = 10;
    private int XP;
    private boolean hasDoneAnything;

    public Unit(Tile tile, Civilization civilization) {

    }

    public int getHealth() {
        return health;
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

    private void openNewArea() {
        for(int i =0 ;i <6;i++)
        {
            int neighbourX = currentTile.getNeighbours(i).getX();
            int neighbourY = currentTile.getNeighbours(i).getY();
            civilization.tileConditions[neighbourX][neighbourY]  = new Civilization.TileCondition(currentTile.getNeighbours(i).CloneTile(), true);
            if(currentTile.getNeighbours(i).getTileType()== TileType.MOUNTAIN ||
                    currentTile.getNeighbours(i).getTileType()== TileType.HILL ||
                    currentTile.getNeighbours(i).getFeature().getFeatureType() == FeatureType.FOREST ||
                    currentTile.getNeighbours(i).getFeature().getFeatureType() == FeatureType.DENSEFOREST)
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
        Tile[] tiles = GameController.getMap().findNextTile(currentTile,movementPrice,destinationTile,this instanceof Civilian);
        this.destinationTile= destinationTile;

        if(tiles==null)
        {
            this.destinationTile=null;
            return false;
        }
        if(tiles.length==1)
            this.destinationTile = null;
        if(this instanceof NonCivilian)
        {
            this.currentTile.setNonCivilian(null);
            tiles[tiles.length-1].setNonCivilian((NonCivilian) this);
        }
        else
        {
            this.currentTile.setCivilian(null);
            tiles[tiles.length-1].setCivilian((Civilian) this);
        }
        this.currentTile = tiles[tiles.length-1];
        openNewArea();
        return true;
    }
}
