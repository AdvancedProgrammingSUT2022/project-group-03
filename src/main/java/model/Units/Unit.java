package model.Units;

import controller.GameController;
import model.Civilization;
import model.Map;
import model.productable;
import model.resources.Resource;
import model.technologies.Technology;
import model.technologies.TechnologyType;
import model.tiles.Tile;

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
    public void setTheNumbers() {

    }

    private void OpenNewArea() {


    }

    private void move(Tile destinationTile) {
        Tile[] tiles = GameController.getMap().findNextTile(currentTile,movementPrice,destinationTile);
        if(tiles==null)
            return;
        if(this instanceof NonCivilian)
        {
            this.currentTile.setNonCivilian(null);
            destinationTile.setNonCivilian((NonCivilian) this);
        }
        else
        {
            this.currentTile.setCivilian(null);
            destinationTile.setCivilian((Civilian) this);
        }
        this.currentTile = destinationTile;
    }
}
