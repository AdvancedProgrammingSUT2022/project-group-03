package view.gameMenu;

import com.beust.jcommander.JCommander;
import controller.gameController.GameController;
import model.City;
import model.Units.NonCivilian;
import model.Units.Unit;
import model.Units.UnitState;
import view.*;

import java.util.ArrayList;

public class GameMenu extends MutatedMenu {


    public static void printMilitaryOverview() {
        ArrayList<Unit> units = GameController.getCivilizations().get(GameController.getPlayerTurn()).getUnits();
        for (Unit unit : units)
            printUnitInfo(unit);
        if (units.size() == 0)
            System.out.println("you don't have any units right now");
    }

    static void printUnitInfo(Unit unit) {
        System.out.print(unit.getUnitType() +
                ": | Health: " + unit.getHealth() +
                " | currentX: " + unit.getCurrentTile().getX() +
                " | currentY: " + unit.getCurrentTile().getY() +
                " | defense Strength: " + unit.getCombatStrength(false) +
                " | attack Strength: " + unit.getCombatStrength(true) +
                " | movementPoint: " + unit.getMovementPrice() +
                " | state: " + unit.getState());
        if (unit instanceof NonCivilian && (unit.getState() == UnitState.FORTIFY ||
                unit.getState() == UnitState.SETUP))
            System.out.print(" | cycle: " + ((NonCivilian) unit).getFortifiedCycle());
        if (unit.getDestinationTile() != null)
            System.out.print(" | destinationX: " + unit.getDestinationTile().getX()
                    + " destinationY: " + unit.getDestinationTile().getY());
        System.out.println();
    }

    public static void infoEconomic() {
        for (City city : GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities())
            CitiesList.cityBanner(city);
    }

    protected JCommander jCommander() {
        JCommander jCommander = new JCommander();
        jCommander.setAllowAbbreviatedOptions(false);
        jCommander.addCommand("city", new CityCommands.InnerClass());
        jCommander.addCommand("cheat", new CheatCommands.InnerClass());
        jCommander.addCommand("info", new InfoCommands.InnerClass());
        jCommander.addCommand("map", new MapCommands.InnerClass());
        jCommander.addCommand("menu", new menuCommands.InnerClass());
        jCommander.addCommand("unit", new UnitStateView.InnerClass());
        jCommander.addCommand("increase", new IncreaseView.InnerClass());
        jCommander.addCommand("select", new TileXAndYFlagSelectUnit.InnerClass());
        jCommander.addCommand("next-turn", new FreeFlagCommands.InnerClass());
        return jCommander;
    }
}
