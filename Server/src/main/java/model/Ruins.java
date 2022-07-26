package model;

import controller.gameController.GameController;
import controller.gameController.UnitStateController;
import model.Units.NonCivilian;
import model.Units.UnitType;
import model.technologies.Technology;
import model.technologies.TechnologyType;
import model.tiles.Tile;
import network.MySocketHandler;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Ruins implements Serializable {
    private final int type;
    private final Tile tile;
    private final ArrayList<Civilization> civilizations = new ArrayList<>();

    public Ruins(int type, Tile tile) {
        this.tile = tile;
        this.type = type;
    }

    public void open(Civilization civilization, GameController gameController, UnitStateController unitStateController, MySocketHandler socketHandler) {
        Random random = new Random();
        tile.setRuins(null);

        switch (type)
        {
            case 0->{
                Technology technology = new Technology(TechnologyType.VALUES.get(random.nextInt(TechnologyType.VALUES.size() - 1)));
                technology.setRemainedCost(0);
                for (Technology research : civilization.getResearches()) {
                    if(research.getTechnologyType()==technology.getTechnologyType())
                    {
                        civilization.getResearches().remove(research);
                        break;
                    }
                }
                civilization.getResearches().add(technology);
                socketHandler.sendUpdate("errorMaker","Congratulations!" +";;"+"you've found: " +technology.getTechnologyType() + ";;"+"i");
            }
            case 1->{
                for (int i = 0; i < 3; i++) {
                    Tile tile = gameController.getMap().randomTile();
                    if(!civilization.getNoFogs().contains(tile) && tile.getCivilization()!=civilization) {
                        civilization.getNoFogs().add(tile);
                        gameController.openNewArea(tile,civilization,null);
                    }
                    else i--;
                }
                socketHandler.sendUpdate("errorMaker","Congratulations!" +";;"+"now you have some tiles with no fogs" + ";;"+"i");


            }
            case 2-> {
                civilization.getCities().get(0).addPopulation();
                socketHandler.sendUpdate("errorMaker","Congratulations!" +";;"+"You have found: +1 population" + ";;"+"i");
            }
            case 3-> {
                int rand = random.nextInt(300);
                civilization.addGold(rand);
                socketHandler.sendUpdate("errorMaker","Congratulations!" +";;"+"you have found: " + rand + " golds" + ";;"+"i");
            }
            case 4->{
                Tile finalTile = null;
                if (unitStateController.isMapMoveValid(tile, civilization) && tile.getCivilian() == null)
                    finalTile = tile;
                else {
                    for (int i = 0; i < 6; i++)
                        if (tile.getNeighbours(i) != null
                                && unitStateController.isMapMoveValid(tile.getNeighbours(i), civilization)
                                && tile.getNeighbours(i).getCivilian() == null) {
                            finalTile = tile.getNeighbours(i);
                            break;
                        }
                }
                NonCivilian nonCivilian;
                if (random.nextInt(100)%2==0) {
                    nonCivilian = new NonCivilian(finalTile, gameController.getCivilizations().get(gameController.getPlayerTurn()), UnitType.SETTLER);
                    socketHandler.sendUpdate("errorMaker","Congratulations!" +";;"+"you have found: 1 settler" + ";;"+"i");
                }
                else {
                    nonCivilian = new NonCivilian(finalTile, gameController.getCivilizations().get(gameController.getPlayerTurn()), UnitType.WORKER);
                    socketHandler.sendUpdate("errorMaker","Congratulations!" +";;"+"you have found: 1 worker" + ";;"+"i");
                }
                assert finalTile != null;
                finalTile.setNonCivilian(nonCivilian,socketHandler);
            }
        }
    }

    public ArrayList<Civilization> getCivilizations() {
        return civilizations;
    }
}
