package com.example.demo.model;

import com.example.demo.controller.gameController.GameController;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Savings implements Serializable {
    @Serial
    private static final long serialVersionUID = 1113799434508676095L;
    private final ArrayList<Civilization> civilizations;
    private final ArrayList<Tasks> unfinishedTasks;
    private final int playerTurn;
    private final Map map;
    private final int x;
    private final int y;
    private Date date;

    public Savings(GameController gameController) {
        civilizations = gameController.getCivilizations();
        unfinishedTasks = gameController.getUnfinishedTasks();
        playerTurn = gameController.getPlayerTurn();
        map = gameController.getMap();
        x = gameController.getMap().getX();
        y = gameController.getMap().getY();
        this.date = new Date();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if(map.getTiles()[i][j].getCivilian() != null )
                    System.out.println("tile "+i+","+j+"    :" +map.getTiles()[i][j].getCivilian().getUnitType());
            }
        }

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
