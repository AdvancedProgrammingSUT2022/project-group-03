package view;

import controller.GameController;
import model.City;
import model.improvements.view.Menu;
import model.technologies.Technology;

import java.util.ArrayList;

public class CitiesList extends Menu {
    {
        regexes = new String[]{
                "^menu exit$",
                "^menu show-current$",
                "^back$",
                "^(\\d+)$",
                "^print cities$",

        };
    }

    public void printCities()
    {
        for (int i = 0; i < GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities().size(); i++)
            System.out.println(i+1 + GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities().get(i).getName());
    }
    @Override
    protected boolean commands(String command) {
        commandNumber = getCommandNumber(command, regexes);
        switch (commandNumber) {
            case -1:
                System.out.println("invalid command");
                break;
            case 0: {
                nextMenu = -1;
                return true;
            }
            case 1:
                System.out.println("Login Menu");
                break;
            case 2:
            {
                System.out.println("technology menu closed successfully");
                return true;
            }
            case 3:
                break;
            case 4:
                printCities();
                break;

        }
        return false;
    }
}
