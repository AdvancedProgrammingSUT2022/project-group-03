package controller;

import model.Civilization;
import model.Map;

import java.util.ArrayList;

public class GameController {
    ArrayList<Civilization> civilizations;
    Map map;
    private int PlayerTurn = 0;
    private void run(String[] PlayersNames)
    {
        setCivilizations(PlayersNames);
        map = new Map(civilizations);
        while (true)
        {
            if(isGameOver())
                break;

        }

    }

    public void setCivilizations(String[] PlayersNames) {

    }

    private void setTheNumbers()
    {
        for()

    }
    private boolean isGameOver()
    {
        return true;
    }
}
