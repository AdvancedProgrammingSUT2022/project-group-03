package com.example.demo.model;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.controller.gameController.UnitStateController;
import com.example.demo.model.Units.NonCivilian;
import com.example.demo.model.Units.UnitType;
import com.example.demo.model.technologies.Technology;
import com.example.demo.model.technologies.TechnologyType;
import com.example.demo.model.tiles.Tile;
import com.example.demo.view.StageController;
import javafx.scene.control.Alert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Ruins  implements Serializable {
    private final int type;
    private final Tile tile;
    private final ArrayList<Civilization> civilizations = new ArrayList<>();

    public Ruins(int type, Tile tile) {
        this.tile = tile;
        this.type = type;
    }

    public void open(Civilization civilization) {
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
                StageController.errorMaker("Congratulations!","you've found: " +technology.getTechnologyType(), Alert.AlertType.INFORMATION);
            }
            case 1->{
                for (int i = 0; i < 3; i++) {
                    Tile tile = GameController.getMap().randomTile();
                    if(!civilization.getNoFogs().contains(tile) && tile.getCivilization()!=civilization) {
                        civilization.getNoFogs().add(tile);
                        GameController.openNewArea(tile,civilization,null);
                    }
                    else i--;
                }
                StageController.errorMaker("Congratulations!","now you have some tiles with no fogs", Alert.AlertType.INFORMATION);

            }
            case 2-> {
                civilization.getCities().get(0).addPopulation();
                StageController.errorMaker("Congratulations!","You have found: +1 population", Alert.AlertType.INFORMATION);
            }
            case 3-> {
                int rand = random.nextInt(300);
                civilization.addGold(rand);
                StageController.errorMaker("Congratulations!","you have found: " + rand + " golds", Alert.AlertType.INFORMATION);
            }
            case 4->{
                Tile finalTile = null;
                if (UnitStateController.isMapMoveValid(tile, civilization) && tile.getCivilian() == null)
                    finalTile = tile;
                else {
                    for (int i = 0; i < 6; i++)
                        if (tile.getNeighbours(i) != null
                                && UnitStateController.isMapMoveValid(tile.getNeighbours(i), civilization)
                                && tile.getNeighbours(i).getCivilian() == null) {
                            finalTile = tile.getNeighbours(i);
                            break;
                        }
                }
                NonCivilian nonCivilian;
                if (random.nextInt(100)%2==0) {
                    nonCivilian = new NonCivilian(finalTile, GameController.getCivilizations().get(GameController.getPlayerTurn()), UnitType.SETTLER);
                    StageController.errorMaker("Congratulations!","you have found: 1 settler", Alert.AlertType.INFORMATION);
                }
                else {
                    nonCivilian = new NonCivilian(finalTile, GameController.getCivilizations().get(GameController.getPlayerTurn()), UnitType.WORKER);
                    StageController.errorMaker("Congratulations!","you have found: 1 worker", Alert.AlertType.INFORMATION);
                }
                assert finalTile != null;
                finalTile.setNonCivilian(nonCivilian);
            }
        }
    }

    public ArrayList<Civilization> getCivilizations() {
        return civilizations;
    }
}
