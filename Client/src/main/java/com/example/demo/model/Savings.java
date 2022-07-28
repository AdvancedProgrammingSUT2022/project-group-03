package com.example.demo.model;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.view.model.GraphicTile;

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
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if(map.getTiles()[i][j].getCivilian() != null )
                System.out.println("tile "+i+","+j+"    :" +map.getTiles()[i][j].getCivilian().getUnitType());
            }
        }
        System.out.println(civilizations.size());
        System.out.println(date);
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
