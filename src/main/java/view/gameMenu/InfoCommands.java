package view.gameMenu;

import com.beust.jcommander.Parameter;
import controller.gameController.GameController;
import controller.TechnologyAndProductionController;
import model.City;
import model.Civilization;
import model.Units.UnitType;
import model.improvements.ImprovementType;
import model.resources.ResourcesTypes;
import model.technologies.Technology;
import view.Runnable;

public class InfoCommands {

    static class InnerClass implements Runnable {
        @Parameter(names = {"--type", "-t"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = true)
        String info = "init";
        @Parameter(names = {"selected", "s"},
                description = "Id of the Customer who's using the services")
        boolean selected = false;

        public int run(String name) {
            switch (info) {
                case "researches", "r":
                    infoResearches();
                    return 3;
                case "units", "u":
                    infoUnits(selected);
                    return 3;
                case "city", "c":
                    CitiesList citiesList = new CitiesList();
                    citiesList.printCities();
                    citiesList.run(GameMenu.scanner);
                    return 3;
                case "economic", "e":
                    GameMenu.infoEconomic();
                    return 3;
                case "demographic", "d":
                    infoDemographic();
                    return 3;
                case "military", "m":
                    GameMenu.printMilitaryOverview();
                    return 3;
                default:
                    System.out.println("invalid command");
            }
            return 3;
        }
    }

    private static void infoResearches() {
        Technology technology =
                GameController.getCivilizations().get(GameController.getPlayerTurn()).getGettingResearchedTechnology();
        if (technology == null) {
            System.out.println("you don't have any technology in the development right now");
            return;
        }
        StringBuilder tempString;
        tempString = new StringBuilder(technology.getTechnologyType() + ": (");
        int cyclesToComplete = TechnologyAndProductionController.cyclesToComplete(technology);
        if (cyclesToComplete == 12345)
            tempString.append("never, your science is 0)");
        else
            tempString.append(cyclesToComplete).append(" cycles to complete)");
        System.out.println(tempString);
        System.out.println("what unlocks after:");
        for (int i = 0; i < UnitType.VALUES.size(); i++)
            if (UnitType.VALUES.get(i).technologyRequired == technology.getTechnologyType())
                System.out.print(UnitType.VALUES.get(i) + " |");
        for (int i = 0; i < ResourcesTypes.VALUES.size(); i++)
            if (ResourcesTypes.VALUES.get(i).technologyTypes == technology.getTechnologyType())
                System.out.print(ResourcesTypes.VALUES.get(i) + " |");
        for (int i = 0; i < ImprovementType.VALUES.size(); i++)
            if (ImprovementType.VALUES.get(i).prerequisitesTechnologies == technology.getTechnologyType())
                System.out.print(ImprovementType.VALUES.get(i) + " |");
        System.out.println();
    }

    private static void infoUnits(boolean selected) {
        if (selected) {
            if (GameController.getSelectedUnit() == null)
                System.out.println("no unit selected");
            else
                GameMenu.printUnitInfo(GameController.getSelectedUnit());
        } else {
            UnitsList unitsList = new UnitsList();
            unitsList.printUnits();
            unitsList.run(GameMenu.scanner);
        }
    }

    private static void infoDemographic() {
        Civilization civilization = GameController.getCivilizations().get(GameController.getPlayerTurn());
        System.out.print("nickname: " + civilization.getUser().getNickname());
        City capital = civilization.getCapital();
        if (capital != null)
            System.out.print(" | capital: " + capital.getName());
        System.out.print(" | science: " + civilization.collectScience() +
                " | happiness: " + civilization.getHappiness());
        if (civilization.getHappiness() < 0)
            System.out.print(", unhappy");
        if (civilization.getHappiness() == 0)
            System.out.print(", breathing");
        if (civilization.getHappiness() > 3)
            System.out.print(", happy");
        System.out.println(" | gold: " + civilization.getGold() +
                " | units: " + civilization.getUnits().size() +
                " | size: " + civilization.getSize());
        System.out.println("resources: ");
        civilization.getResourcesAmount().forEach((k, v) -> {
            if (v != 0)
                System.out.println(k + ": " + v);
        });
        System.out.println("luxury resources: ");
        civilization.getUsedLuxuryResources().forEach((k, v) -> {
            if (v)
                System.out.print(k + " |");
        });
    }
}
