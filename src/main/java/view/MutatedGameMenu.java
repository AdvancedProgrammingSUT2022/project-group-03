package view;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import controller.GameController;
import controller.TechnologyController;
import model.City;
import model.Civilization;
import model.Map;
import model.Units.Unit;
import model.Units.UnitType;
import model.improvements.ImprovementType;
import model.resources.ResourcesTypes;
import model.technologies.Technology;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;

public class MutatedGameMenu extends MutatedMenu {
    static class FreeFlagCommands implements Runnable {
        public int run(String name) {
            switch (name) {
                case "next-turn":
                    if (GameController.nextTurnIfYouCan())
                        System.out.println("turn ended successfully");
                    else {
                        System.out.print("failed to end the turn: " + GameController.getUnfinishedTasks().get(0).getTaskTypes());
                        if (GameController.getUnfinishedTasks().get(0).getTile() == null)
                            System.out.println();
                        else System.out.println(" | x: " + GameController.getUnfinishedTasks().get(0).getTile().getX() +
                                " y: " + GameController.getUnfinishedTasks().get(0).getTile().getY());
                    }
                    return 3;
                default:
                    System.out.println("invalid command");

            }
            return 3;
        }

    }



    static class tileXAndYFlagSelectUnit implements Runnable {
        @Parameter(names = {"--tilex", "-x"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        int x = -1989;
        @Parameter(names = {"--tiley", "-y"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        int y = -1989;
        @Parameter(names = {"--object", "-o"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = true)
        String type;
        @Parameter(names = {"--name", "-n"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        String name = "init";

        public int run(String name) {
            if ((type.equals("noncivilian") || type.equals("ncu")) && x != -1989 && y != -1989) {
                if (GameController.setSelectedCombatUnit(x, y))
                    System.out.println("combat unit selected successfully");
                else
                    System.out.println("selection failed");
            } else if ((type.equals("civilian") || type.equals("cu")) && x != -1989 && y != -1989) {
                if (GameController.setSelectedNonCombatUnit(x, y))
                    System.out.println("noncombat unit selected successfully");
                else
                    System.out.println("selection failed");
            } else if ((type.equals("city") || type.equals("c")) && x != -1989 && y != -1989) {
                if (!GameController.setSelectedCityByPosition(x, y))
                    System.out.println("city does not exist");
                else
                    System.out.println("city selected successfully");
            } else if ((type.equals("city") || type.equals("c")) && !name.equals("init")) {
                if (!GameController.setSelectedCityByName(name))
                    System.out.println("city does not exist");
                else
                    System.out.println("city selected successfully");
            } else System.out.println("invalid command");
            return 3;
        }

    }

    static class increase implements Runnable {
        @Parameter(names = {"--amount", "-a"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = true)
        int amount;
        @Parameter(names = {"--type", "-t"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = true)
        String type;

        public int run(String name) {
            if (type.equals("gold") || type.equals("g")) {
                GameController.getCivilizations().get(GameController.getPlayerTurn())
                        .increaseGold(amount);
                System.out.println("cheat activated successfully");
            } else if (type.equals("turn") || type.equals("t")) {
                for (int i = 0; i < amount * GameController.getCivilizations().size(); i++)
                    GameController.nextTurn();
                System.out.println("cheat activated successfully");
            } else System.out.println("invalid command");
            return 3;
        }


    }

    static class unitState implements Runnable {

        @Parameter(names = {"--state", "-s"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        String state = "init";
        @Parameter(names = {"do", "-d"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        String move = "init";
        @Parameter(names = {"--obj", "-o"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        String object = "init";
        @Parameter(names = {"--tilex", "-x"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        int x = -1989;
        @Parameter(names = {"--tiley", "-y"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        int y = -1989;

        public int run(String name) {
            boolean choose = false;
            switch (state) {
                case "sleep":
                    switch (GameController.unitSleep()) {
                        case 0:
                            System.out.println("the selected unit has been set to sleep successfully");
                            return 3;
                        case 1:
                            System.out.println("no unit is selected");
                            return 3;
                        case 2:
                            System.out.println("the selected unit is not yours");
                            return 3;
                    }
                    return 3;
                case "alert":
                    switch (GameController.unitAlert()) {
                        case 0:
                            System.out.println("the selected unit has been set to alert successfully");
                            return 3;
                        case 1:
                            System.out.println("no unit is selected");
                            return 3;
                        case 2:
                            System.out.println("the selected unit is not yours");
                            return 3;
                        case 3:
                            System.out.println("you can't go to alert position when there is enemy around");
                            return 3;
                    }
                    return 3;
                case "fortify":
                    switch (GameController.unitFortify()) {
                        case 0:
                            System.out.println("the selected unit has been set to fortify successfully");
                            return 3;
                        case 1:
                            System.out.println("no unit is selected");
                            return 3;
                        case 2:
                            System.out.println("the selected unit is not yours");
                            return 3;
                    }
                    return 3;
                case "fortify_until_heal":
                    switch (GameController.unitFortifyUntilFullHealth()) {
                        case 0:
                            System.out.println("the selected unit has been set to fortifyUntilFullHealth successfully");
                            return 3;
                        case 1:
                            System.out.println("no unit is selected");
                            return 3;
                        case 2:
                            System.out.println("the selected unit is not yours");
                            return 3;
                    }
                    return 3;
                case "garrison":
                    switch (GameController.unitGarrison()) {
                        case 0:
                            System.out.println("the selected unit has been set to garrison successfully");
                            return 3;
                        case 1:
                            System.out.println("no unit is selected");
                            return 3;
                        case 2:
                            System.out.println("the selected unit is not yours");
                            return 3;
                    }
                    return 3;
                case "wake":
                    switch (GameController.unitWake()) {
                        case 0:
                            System.out.println("the selected unit has been awaken successfully");
                            return 3;
                        case 1:
                            System.out.println("no unit is selected");
                            return 3;
                        case 2:
                            System.out.println("the selected unit is not yours");
                            return 3;
                        case 3:
                            System.out.println("the selected unit is already awake");
                            return 3;
                    }
                    return 3;
                case "init":
                    switch (move) {
                        case "init":
                            System.out.println("invalid command");
                            return 3;
                        case "moveto":
                            if (x == -1989 || y == -1989) System.out.println("This command needs x and y");
                            else {
                                if (GameController.unitMoveTo(x, y))
                                    System.out.println("Moved successfully");
                                else
                                    System.out.println("Moving failed");
                                return 3;
                            }
                            return 3;
                        case "attack":
                            if (x == -1989 || y == -1989) System.out.println("This command needs x and y");
                            else {
                                switch (GameController.unitAttack(x, y)) {
                                    case 0:
                                        System.out.println("Attacked successfully");
                                        break;
                                    case 1:
                                        System.out.println("There is nothing to attack");
                                        break;
                                    case 2:
                                        System.out.println("Out of bond tile");
                                        break;
                                    case 3:
                                        System.out.println("Select your unit first");
                                        break;
                                    case 4:
                                        System.out.println("Can't find a route");
                                }
                            }
                            return 3;
                        case "foundcity":
                            switch (GameController.unitWake()) {
                                case 0:
                                    System.out.println("the selected unit has been awaken successfully");
                                    return 3;
                                case 1:
                                    System.out.println("no unit is selected");
                                    return 3;
                                case 2:
                                    System.out.println("the selected unit is not yours");
                                    return 3;
                                case 3:
                                    System.out.println("the selected unit is already awake");
                                    return 3;
                            }
                            return 3;
                        case "pillage":
                            switch (GameController.unitPillage()) {
                                case 4:
                                    System.out.println("Select your unit first");
                                    break;
                                case 3:
                                    System.out.println("Nothing to pillage");
                                    break;
                                case 0:
                                    System.out.println("Pillaged successfully");
                            }
                            return 3;
                        case "skip":
                            switch (GameController.skipUnitTask()) {
                                case 0:
                                    System.out.println("task skipped successfully");
                                    return 3;
                                case 1:
                                    System.out.println("no unit is selected");
                                    return 3;
                                case 2:
                                    System.out.println("the selected unit is not yours");
                                    return 3;
                                case 3:
                                    System.out.println("the selected unit does not need a task");
                                    return 3;
                            }
                            return 3;
                        case "delete":
                            switch (GameController.unitDelete(GameController.getSelectedUnit())) {
                                case 0:
                                    System.out.println("the selected unit has been deleted successfully");
                                    return 3;
                                case 1:
                                    System.out.println("no unit is selected");
                                    return 3;
                                case 2:
                                    System.out.println("the selected unit is not yours");
                                    return 3;
                            }
                            return 3;
                        case "cancel_mission":
                            switch (GameController.unitCancelMission()) {
                                case 0:
                                    System.out.println("mission cancelled successfully");
                                    return 3;
                                case 1:
                                    System.out.println("not unit is selected");
                                    return 3;
                                case 2:
                                    System.out.println("the selected unit is not yours");
                                    return 3;
                                case 3:
                                    System.out.println("the selected unit has no missions");
                                    return 3;
                            }
                            return 3;
                        case "remove":
                            if (object.equals("jungle")) choose = true;
                            else if (object.equals("route")) choose = false;
                            else return 3;
                            switch (GameController.unitRemoveFromTile(choose)) {
                                case 0:
                                    System.out.println(object + "'s removal from the tile operation started successfully");
                                    return 3;
                                case 1:
                                    System.out.println("no unit is selected");
                                    return 3;
                                case 2:
                                    System.out.println("the selected unit is not yours");
                                    return 3;
                                case 3:
                                    System.out.println("the selected unit is not a worker");
                                    return 3;
                                case 4:
                                    System.out.println("the selected tile does not have a jungle");
                                    return 3;
                                case 5:
                                    System.out.println("the selected tile does not have any road or railroad");
                                    return 3;
                            }
                            return 3;
                        case "repair":
                            switch (GameController.unitRepair()) {
                                case 0:
                                    System.out.println("improvement repaired successfully");
                                    return 3;
                                case 1:
                                    System.out.println("no unit is selected");
                                    return 3;
                                case 2:
                                    System.out.println("the selected unit is not yours");
                                    return 3;
                                case 3:
                                    System.out.println("the selected unit is not a worker");
                                    return 3;
                                case 4:
                                    System.out.println("there are no improvements here");
                                    return 3;
                                case 5:
                                    System.out.println("the selected improvement does not need repairing");
                                    return 3;
                            }
                            return 3;
                        default:
                            System.out.println("invalid command");


                    }
                default:
                    System.out.println("invalid command");


            }
            return 3;
        }

    }

    static class menuCommands implements Runnable {
        @Parameter(names = {"exit"},
                description = "Id of the Customer who's using the services",
                required = false)
        boolean exit = false;
        @Parameter(names = {"show-current"},
                description = "Id of the Customer who's using the services",
                required = false)
        boolean show = false;
        @Parameter(names = {"enter"},
                description = "Id of the Customer who's using the services",
                required = false)
        String nextmenu = "init";

        public int run(String name) {
            if (exit && !show && nextmenu.equals("init")) {
                return 2;
            } else if (show && nextmenu.equals("init")) {
                System.out.println("Game Menu");
            } else if (nextmenu.equals("Technologies")) {
                if (GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities().size() == 0)
                    System.out.println("you need at least one city to enter the technology menu");
                else {
                    ChooseTechnology chooseTechnology = new ChooseTechnology();
                    chooseTechnology.printDetails();
                    chooseTechnology.run(scanner);
                }
            } else if (nextmenu.equals("CityProduction")) {
                if (GameController.getSelectedCity() == null)
                    System.out.println("no city is selected");
                else if (GameController.getSelectedCity().getCivilization() != GameController.getCivilizations().get(GameController.getPlayerTurn()))
                    System.out.println("the selected city is not yours");
                else {
                    ProductionCityMenu productionCityMenu = new ProductionCityMenu();
                    productionCityMenu.printDetails();
                    productionCityMenu.run(scanner);
                }
            } else {
                System.out.println("invalid command");
            }
            return 3;
        }
    }

    static class mapCommands implements Runnable {
        @Parameter(names = {"print"},
                description = "Id of the Customer who's using the services",
                required = false)
        boolean print = false;
        @Parameter(names = {"show"},
                description = "Id of the Customer who's using the services",
                required = false)
        boolean show = false;
        @Parameter(names = {"--city", "-c"},
                description = "Id of the Customer who's using the services",
                required = false)
        String city = "init";
        @Parameter(names = {"--tilex", "-x"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        int x = -1989;
        @Parameter(names = {"--tiley", "-y"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        int y = -1989;
        @Parameter(names = {"--direction", "-d"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        String dir = "init";
        @Parameter(names = {"move", "-m"},
                description = "Id of the Customer who's using the services",
                required = false)
        boolean move = false;
        @Parameter(names = {"--amount", "-a"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        int amount = -1989;

        public int run(String name) {
            if (print && !move && !show && city.equals("init")) {
                System.out.println(GameController.printMap());
            } else if (show && !city.equals("init") && (x == -1989 && y == -1989) && !move) {
                int result = GameController.mapShowCityName(city);
                if (result == 1)
                    System.out.println("no city with this name exists");
                if (result == 2)
                    System.out.println("you have not unlocked the position of this city yet");
                if (result == 0)
                    System.out.println("city selected successfully");
                System.out.println(GameController.printMap());
            } else if (show && !move && (x != -1989 && y != -1989)) {
                GameController.mapShowPosition(x - Map.WINDOW_X / 2,
                        y - Map.WINDOW_Y / 2 + 1);
                System.out.println(GameController.printMap());
            } else if (move) {
                if (dir.matches("[RULD]")) {
                    if (amount == -1989)
                        GameController.mapMove(1, dir);
                    else GameController.mapMove(amount, dir);
                    System.out.println("map moved successfully");
                }
            } else {
                System.out.println("invalid command");
            }
            return 3;
        }
    }

    static class infoCommands implements Runnable {
        @Parameter(names = {"--object", "-o"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        String info = "init";

        public int run(String name) {
            switch (info) {
                case "researches":
                case "r":
                    Technology technology =
                            GameController.getCivilizations().get(GameController.getPlayerTurn()).getGettingResearchedTechnology();
                    if (technology == null) {
                        System.out.println("you don't have any technology in the development right now");
                        return 3;
                    }
                    StringBuilder tempString;
                    tempString = new StringBuilder(technology.getTechnologyType() + ": (");
                    int cyclesToComplete = TechnologyController.cyclesToComplete(technology);
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
                    return 3;
                case "units":
                case "u":
                    UnitsList unitsList = new UnitsList();
                    unitsList.printUnits();
                    unitsList.run(scanner);
                    return 3;
                case "city":
                case "c":
                    CitiesList citiesList = new CitiesList();
                    citiesList.printCities();
                    citiesList.run(scanner);
                    return 3;
                case "economic":
                case "e":
                    for (City city : GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities())
                        CitiesList.cityBanner(city);
                    return 3;
                case "demographic":
                case "d":
                    Civilization civilization = GameController.getCivilizations().get(GameController.getPlayerTurn());
                    System.out.print("nickname: " + civilization.getUser().getNickname());
                    City capital = civilization.getCapital();
                    if (capital != null)
                        System.out.print(" | capital: " + capital.getName());
                    System.out.println(" | science: " + civilization.collectScience() +
                            " | happiness: " + civilization.getHappiness() +
                            " | gold: " + civilization.getGold() +
                            " | units: " + civilization.getUnits().size() +
                            " | size: " + civilization.getSize());
                    System.out.println("resources: ");
                    civilization.getResourcesAmount().forEach((k, v) -> {
                        if (v != 0)
                            System.out.println(k + ": " + v);
                    });
                    System.out.print("luxury resources: ");
                    civilization.getUsedLuxuryResources().forEach((k, v) -> {
                        if (v)
                            System.out.print(k + " |");
                    });
                    return 3;
                default:
                    System.out.println("invalid command");

            }
            return 3;
        }

    }

    static class cheatCommands implements Runnable {
        @Parameter(names = {"--amount", "-a"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        int amount = -1989;
        @Parameter(names = {"--object", "-o"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        String obj = "init";
        @Parameter(names = {"science", "-s"},
                description = "Id of the Customer who's using the services",
                required = false)
        boolean science = false;
        @Parameter(names = {"production", "-p"},
                description = "Id of the Customer who's using the services",
                required = false)
        boolean production = false;
        @Parameter(names = {"resource", "-r"},
                description = "Id of the Customer who's using the services",
                required = false)
        boolean resource = false;
        @Parameter(names = {"create"},
                description = "Id of the Customer who's using the services",
                required = false)
        String unit = "init";
        @Parameter(names = {"--tilex", "-x"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        int x = -1989;
        @Parameter(names = {"--tiley", "-y"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        int y = -1989;

        public int run(String name) {
            if (science && amount != -1989) {
                GameController.cheatScience(amount);
            } else if (production && amount != -1989) {
                GameController.cheatProduction(amount);
            } else if (resource && amount != -1989 && !obj.equals("init")) {
                GameController.cheatResource(amount, ResourcesTypes.stringToEnum(obj));
            } else if (!unit.equals("init") && (x != -1989 && y != -1989)) {
                UnitType unitType = UnitType.stringToEnum(obj);
                GameController.cheatUnit(x, y, unitType);
            } else {
                System.out.println("invalid command");
            }
            return 3;
        }

    }

    static class cityCommands implements Runnable {
        @Parameter(names = {"show"},
                description = "Id of the Customer who's using the services",
                required = false)
        String show = "init";
        @Parameter(names = {"start"},
                description = "Id of the Customer who's using the services",
                required = false)
        String startProducing = "init";
        @Parameter(names = {"--type","-t"},
                description = "Id of the Customer who's using the services",
                required = false)
        String type = "init";
        @Parameter(names = {"buy"},
                description = "Id of the Customer who's using the services",
                required = false)
        String buy = "init";
        @Parameter(names = {"citizen"},
                description = "Id of the Customer who's using the services",
                required = false)
        String citizen = "init";
        @Parameter(names = {"build"},
                description = "Id of the Customer who's using the services",
                required = false)
        String build = "init";
        @Parameter(names = {"attack"},
                description = "Id of the Customer who's using the services",
                required = false)
        boolean attack = false;
        @Parameter(names = {"burn"},
                description = "Id of the Customer who's using the services",
                required = false)
        boolean burn = false;
        @Parameter(names = {"capture"},
                description = "Id of the Customer who's using the services",
                required = false)
        boolean capture = false;

        @Parameter(names = {"--destinationx", "-dx"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        int dx;
        @Parameter(names = {"--destinationy", "-dy"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        int dy;
        @Parameter(names = {"--tilex", "-x"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        int ox =-1989;
        @Parameter(names = {"--tiley", "-y"},
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        int oy = -1989;
        public int run(String name) {
            if(!show.equals("init")){
                City city = GameController.getSelectedCity();
                if (city == null) {
                    System.out.println("no city is selected");
                    return 3;
                }
                System.out.println("name: " + city.getName());
                System.out.println("founder: " + city.getFounder().getUser().getNickname());
                if (city.getCivilization() == GameController.getCivilizations().get(GameController.getPlayerTurn())) {
                    System.out.println("gold: " + city.getGold());
                    System.out.println("production: " + city.collectProduction());
                    System.out.println("food: " + city.collectFood());
                    System.out.println("population: " + city.getPopulation());
                    System.out.println("citizens: " + city.getCitizen());
                    System.out.print("getting worked on tiles: ");
                    for (int i = 0; i < city.getGettingWorkedOnByCitizensTiles().size(); i++)
                        System.out.print(city.getGettingWorkedOnByCitizensTiles().get(i).getX() + "," +
                                city.getGettingWorkedOnByCitizensTiles().get(i).getY() + "   |   ");
                    System.out.println("\nHP: " + city.getHP());
                    System.out.println("attack strength: " + city.getCombatStrength(true));
                    System.out.println("defence strength: " + city.getCombatStrength(false));
                }

            }
            else if(startProducing.equals("producing")&& !type.equals("init")){
                int result = GameController.startProducingUnit(type);
                if (result == 0)
                    System.out.println("production started successfully");
                if (result == 1)
                    System.out.println("no product with this name is defined");
                if (result == 2)
                    System.out.println("no city is selected");
                if (result == 3)
                    System.out.println("the selected city is not yours");
                if (result == 4)
                    System.out.println("you don't have enough money");
            }
            else if((buy.equals("tile")|| buy.equals("t") && (ox != -1989 && oy != -1989))){
                switch (GameController.buyTile(ox, oy)) {
                    case 0:
                        System.out.println("Tile added successfully");
                        break;
                    case 1:
                        System.out.println("Already has an owner");
                        break;
                    case 2:
                        System.out.println("Select valid tile");
                        break;
                    case 3:
                        System.out.println("Don't go too far");
                        break;
                    case 4:
                        System.out.println("Select your city first");
                        break;
                    case 5:
                        System.out.println("Out of bond Tile");
                        break;
                }
            }
            else if(citizen.equals("work") &&(dx != -1989 && dy != -1989)){
                int x;
                if ((ox != -1989 && oy != -1989))
                    x = GameController.reAssignCitizen(ox,oy,dx,dy);
                else x = GameController.assignCitizen(dx,dy);
                switch (x) {
                    case 0:
                        System.out.println("Assigned successfully");
                        break;
                    case 1:
                        System.out.println("Select valid tile");
                        break;
                    case 2:
                        System.out.println("Not your city");
                        break;
                    case 3:
                        System.out.println("Select a city jackass");
                        break;
                    case 4:
                        System.out.println("Failed");
                        break;
                }
            }
            else if(burn){
                cityDestiny(true);
            }
            else if(capture){
                cityDestiny(false);
            }
            else if(attack && (dx != -1989 && dy != -1989)){
                switch (GameController.cityAttack(dx, dy)) {
                    case 0:
                        System.out.println("Attacked successfully");
                        break;
                    case 1:
                        System.out.println("Can not attack there");
                        break;
                    case 2:
                        System.out.println("Out of bond tile");
                        break;
                    case 3:
                        System.out.println("Select your city first");
                        break;
                    case 4:
                        System.out.println("Out of range");
                        break;
                    case 5:
                        System.out.println("Siege need setup before attack");
                        break;
                }
            }
            else if(build.equals("wall")) {
                int result = GameController.buildWall();
                if (result == 0)
                    System.out.println("wall's production started successfully");
                if (result == 1)
                    System.out.println("no city is selected");
                if (result == 2)
                    System.out.println("the selected city is not yours");
                if (result == 3)
                    System.out.println("the selected city already has a wall");
            }
            else System.out.println("invalid command");
            return 3;


        }
        private void cityDestiny(boolean burn) {
            switch (GameController.cityDestiny(burn)) {
                case 0:
                    if (burn) System.out.println("The city destroyed");
                    else System.out.println("The city captured");
                    break;
                case 1:
                    System.out.println("The city still standing");
                    break;
                case 2:
                    System.out.println("Select a surrendered city first");
                    break;
                case 3:
                    System.out.println("Can not burn a capital");
            }
        }
    }


    protected JCommander jCommander() {
        JCommander jCommander = new JCommander();
        jCommander.setAllowAbbreviatedOptions(true);
        jCommander.addCommand("city", new cityCommands());
        jCommander.addCommand("cheat", new cheatCommands());
        jCommander.addCommand("info", new infoCommands());
        jCommander.addCommand("map", new mapCommands());
        jCommander.addCommand("menu", new menuCommands());
        jCommander.addCommand("unit", new unitState());
        jCommander.addCommand("increase", new increase());
        jCommander.addCommand("select", new tileXAndYFlagSelectUnit());
        jCommander.addCommand("next-turn", new FreeFlagCommands());
        jCommander.addCommand("capture_city", new FreeFlagCommands());
        return jCommander;
    }

    public static void main(String[] args) {
        new MutatedGameMenu().run(new Scanner(System.in), 3);
    }


}