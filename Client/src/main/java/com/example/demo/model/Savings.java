package com.example.demo.model;

import com.example.demo.controller.gameController.GameController;

import java.io.Serializable;
import java.util.ArrayList;

public class Savings implements Serializable {
    private final ArrayList<Civilization> civilizations;
    private final ArrayList<Tasks> unfinishedTasks;
    private final int playerTurn;
    private final Map map;
    private final int x;
    private final int y;

    public Savings() {
        civilizations = GameController.getCivilizations();
        unfinishedTasks = GameController.getUnfinishedTasks();
        playerTurn = GameController.getPlayerTurn();
        map = GameController.getMap();
        x = GameController.getMap().getStaticX();
        y = GameController.getMap().getStaticY();
    }

    public void loadThisToGameController(){
        GameController.setCivilizationsAsList(civilizations);
        GameController.setUnfinishedTasks(unfinishedTasks);
        GameController.setPlayerTurn(playerTurn);
        GameController.setMap(map);
        Map.setStaticX(x);
        Map.setStaticY(y);
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
