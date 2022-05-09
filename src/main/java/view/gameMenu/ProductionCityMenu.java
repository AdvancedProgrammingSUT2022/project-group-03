package view.gameMenu;

import model.Units.*;
import view.Menu;

import java.util.ArrayList;

public class ProductionCityMenu extends Menu {
    {
        regexes = new String[]{
                "^menu exit$",
                "^menu show-current$",
                "^(\\d+)$"
        };
    }

    public final ArrayList<Unit> possibleUnits = new ArrayList<>();


    private boolean createUnit(String command) {
        int number = Integer.parseInt(command);
        if (number - 1 > possibleUnits.size() || number < 1) {
            System.out.println("invalid number");
            return false;
        }
        return CityCommands.startProducingUnit(possibleUnits.get(number - 1)
                .getUnitType().toString());
    }

    @Override
    protected boolean commands(String command) {
        commandNumber = getCommandNumber(command, regexes, true);
        switch (commandNumber) {
            case -1:
                System.out.println("invalid command");
                break;
            case 0:
                nextMenu = 1;
                return true;
            case 1:
                System.out.println("Production Menu");
                break;
            case 2:
                if (createUnit(command))
                    return true;
                break;

        }
        return false;
    }
}
