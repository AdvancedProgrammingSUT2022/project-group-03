package view.gameMenu;

import controller.gameController.GameController;
import controller.gameController.TileXAndYFlagSelectUnitController;
import model.Units.CombatType;
import model.Units.Unit;
import view.Menu;

import java.util.ArrayList;

public class UnitsList extends Menu {
    {
        regexes = new String[]{
                "^menu exit$",
                "^menu show-current$",
                "^(\\d+)$",
        };
    }

    public void printUnits() {
        ArrayList<Unit> units = GameController.getCivilizations()
                .get(GameController.getPlayerTurn()).getUnits();
        for (int i = 0; i < units.size(); i++)
            System.out.println(i + 1 + ", " + units.get(i).getUnitType() + ": "
                    + units.get(i).getState() + "; "
                    + units.get(i).getCurrentTile().getX() + ", "
                    + units.get(i).getCurrentTile().getY() + " | "
                    + units.get(i).getCurrentTile().getTileType());
        if (units.size() == 0)
            System.out.println("you don't have any units right now");
    }

    private boolean selectUnit(String command) {
        int number = Integer.parseInt(command);
        ArrayList<Unit> units = GameController.getCivilizations()
                .get(GameController.getPlayerTurn()).getUnits();
        if (number > units.size() || number < 1) {
            System.out.println("invalid number");
            return false;
        }
        if (units.get(number - 1).getUnitType().combatType == CombatType.CIVILIAN)
            TileXAndYFlagSelectUnitController.setSelectedCivilian(units.get(number - 1)
                    .getCurrentTile().getX(), units.get(number - 1).getCurrentTile().getY());
        else
            TileXAndYFlagSelectUnitController.setSelectedCivilian(units.get(number - 1)
                    .getCurrentTile().getX(), units.get(number - 1).getCurrentTile().getY());
        return true;
    }

    @Override
    protected boolean commands(String command) {
        commandNumber = getCommandNumber(command, regexes, true);
        switch (commandNumber) {
            case -1:
                System.out.println("invalid command");
                break;
            case 0:
                return true;
            case 1:
                System.out.println("Login Menu");
                break;
            case 2:
                if (selectUnit(command))
                    return true;
                break;
            case 3:
                GameMenu.printMilitaryOverview();
                break;
        }
        return false;
    }
}