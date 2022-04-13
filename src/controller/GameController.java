package controller;

import model.City;
import model.Civilization;
import model.Map;
import model.Units.Unit;
import model.tiles.Tile;

import java.util.ArrayList;

public class GameController {
    ArrayList<Civilization> civilizations;
    Unit selectedUnit;
    City selectedCity;
    Map map;
    private int PlayerTurn = 0;

    private void startGame(String[] PlayersNames) {
        setCivilizations(PlayersNames);
        map = new Map(civilizations);
    }

    public void setCivilizations(String[] PlayersNames) {

    }

    private void setTheNumbers() {
        for ()

    }
    private boolean canUnitAttack(Unit unit, Tile tile)
    {

    }

    private boolean canCityAttack(City city, Tile tile)
    {

    }

    private boolean canUnitMove(City city, Tile tile)
    {

    }
    private boolean isGameOver() {
        return true;
    }
}
