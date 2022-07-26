package model;

import controller.gameController.GameController;

import java.io.Serializable;
import java.util.ArrayList;

public class Savings implements Serializable {
    private final ArrayList<Civilization> civilizations;
    private final ArrayList<Tasks> unfinishedTasks;
    private final int playerTurn;
    private final Map map;
    private final int x;
    private final int y;

    public Savings(GameController gameController) {
        civilizations = gameController.getCivilizations();
        unfinishedTasks = gameController.getUnfinishedTasks();
        playerTurn = gameController.getPlayerTurn();
        map = gameController.getMap();
        x = gameController.getMap().getX();
        y = gameController.getMap().getY();
    }

    public void loadThisToGameController(GameController gameController){
        gameController.setCivilizationsAsList(civilizations);
        gameController.setUnfinishedTasks(unfinishedTasks);
        gameController.setPlayerTurn(playerTurn);
        gameController.setMap(map);
        gameController.getMap().setX(x);
        gameController.getMap().setY(y);
    }

    public ArrayList<Civilization> getCivilizations() {
        return civilizations;
    }

    public ArrayList<Tasks> getUnfinishedTasks() {
        return unfinishedTasks;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public Map getMap() {
        return map;
    }
}
