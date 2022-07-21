package com.example.demo.model;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.controller.gameController.UnitStateController;
import com.example.demo.model.Units.NonCivilian;
import com.example.demo.model.Units.UnitType;
import com.example.demo.model.technologies.Technology;
import com.example.demo.model.technologies.TechnologyType;
import com.example.demo.model.tiles.Tile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Ruins implements Serializable {
    private final int type;
    private final Tile tile;
    Object object;
    private ArrayList<Civilization> civilizations = new ArrayList<>();

    public Ruins(int type, Tile tile) {
        this.tile = tile;
        this.type = type;
        Random random = new Random();
        switch (type) {
            //population
            case 4 -> {
                //settler
                if (random.nextInt(2) % 2 == 0)
                    object = -2;
                    //worker
                else object = -3;
            }
        }
    }

    public void open(Civilization civilization) {
        Random random = new Random();
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
            }
            case 1->{
                for (int i = 0; i < 3; i++) {
                    Tile tile = GameController.getMap().randomTile();
                    if(!civilization.getNoFogs().contains(tile) && tile.getCivilization()!=civilization)
                        civilization.getNoFogs().add(tile);
                    else i--;
                }

            }
            case 2-> civilization.getCities().get(0).addPopulation();
            case 3-> civilization.addGold(random.nextInt(300));
            case 4->{
                Tile finalTile = null;
                if (UnitStateController.isMapMoveValid(tile, civilization) && tile.getNonCivilian() == null)
                    finalTile = tile;
                else {
                    for (int i = 0; i < 6; i++)
                        if (tile.getNeighbours(i) != null
                                && UnitStateController.isMapMoveValid(tile.getNeighbours(i), civilization)
                                && tile.getNeighbours(i).getNonCivilian() == null) {
                            finalTile = tile.getNeighbours(i);
                            break;
                        }
                }
                NonCivilian nonCivilian;
                if (random.nextInt(100)%2==0)
                    nonCivilian = new NonCivilian(finalTile, GameController.getCivilizations().get(GameController.getPlayerTurn()), UnitType.SETTLER);
                else
                    nonCivilian = new NonCivilian(finalTile, GameController.getCivilizations().get(GameController.getPlayerTurn()), UnitType.WORKER);
                assert finalTile != null;
                finalTile.setNonCivilian(nonCivilian);
            }
        }
        tile.setRuins(null);
    }

    public ArrayList<Civilization> getCivilizations() {
        return civilizations;
    }
}
