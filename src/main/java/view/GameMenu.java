package view;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import controller.GameController;
import controller.TechnologyController;
import model.City;
import model.Civilization;
import model.Map;
import model.Units.NonCivilian;
import model.Units.Unit;
import model.Units.UnitState;
import model.Units.UnitType;
import model.improvements.ImprovementType;
import model.resources.ResourcesTypes;
import model.technologies.Technology;
import model.technologies.TechnologyType;

import java.util.ArrayList;
import java.util.HashMap;

public class GameMenu extends MutatedMenu {
    static class FreeFlagCommands implements Runnable {
        public int run(String name) {
            if ("next-turn".equals(name)) {
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
            } else {
                System.out.println("invalid command");
            }
            return 3;
        }
    }


    static class tileXAndYFlagSelectUnit implements Runnable {
        @Parameter(names = {"--tilex", "-x"},
                description = "X of the entry tile",
                arity = 1)
        int x = -1989;
        @Parameter(names = {"--tiley", "-y"},
                description = "Y of the entry tile",
                arity = 1)
        int y = -1989;
        @Parameter(names = {"--object", "-o"},
                description = "the entry object",
                arity = 1,
                required = true)
        String type;
        @Parameter(names = {"--name", "-n"},
                description = "the name of the entry",
                arity = 1)
        String name = "init";

        public int run(String command) {
            if ((type.equals("noncivilian") || type.equals("ncu")) && x != -1989 && y != -1989) {
                if (GameController.setSelectedNonCivilian(x, y))
                    System.out.println("noncivilian unit selected successfully");
                else
                    System.out.println("selection failed");
            } else if ((type.equals("civilian") || type.equals("cu")) && x != -1989 && y != -1989) {
                if (GameController.setSelectedCivilian(x, y))
                    System.out.println("civilian unit selected successfully");
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
        @Parameter(names = {"--turn", "-t"},
                description = "the command that chooses changing turns",
                arity = 1,
                required = false)
        int turn = -1989;
        @Parameter(names = {"--gold", "-g"},
                description = "the command that chooses increasing gold",
                arity = 1,
                required = false)
        int gold = -1989;

        public int run(String name) {
            if (gold != -1989) {
                GameController.getCivilizations().get(GameController.getPlayerTurn()).increaseGold(gold);
                System.out.println("cheat activated successfully");
            } else if (turn != 1989) {
                for (int i = 0; i < turn * GameController.getCivilizations().size(); i++)
                    GameController.nextTurn();
                System.out.println("cheat activated successfully");
            } else System.out.println("invalid command");
            return 3;
        }
    }

    static class unitState implements Runnable {

        @Parameter(names = {"--state", "-s"},
                description = "state of the selected unit",
                arity = 1)
        String state = "init";
        @Parameter(names = {"do", "-d"},
                description = "doing an operation like building, repairing and...",
                arity = 1)
        String move = "init";
        @Parameter(names = {"--object", "-o"},
                description = "the entry object",
                arity = 1)
        String object = "init";
        @Parameter(names = {"--name", "-n"},
                description = "the name of the entry",
                arity = 1)
        String nameCity = "init";
        @Parameter(names = {"--tilex", "-x"},
                description = "X of the entry tile",
                arity = 1)
        int x = -1989;
        @Parameter(names = {"--tiley", "-y"},
                description = "Y of the entry tile",
                arity = 1)
        int y = -1989;

        public int run(String name) {
            switch (state) {
                case "sleep":
                    unitSleep();
                    return 3;
                case "alert":
                    unitAlert();
                    return 3;
                case "fortify":
                    unitFortify(false);
                    return 3;
                case "fortify_until_heal":
                    unitFortify(true);
                    return 3;
                case "garrison":
                    unitGarrison();
                    return 3;
                case "wake":
                    unitWake();
                    return 3;
                case "setup":
                    unitSetupRanged();
                    return 3;
                case "init":
                    switch (move) {
                        case "moveto":
                            unitMoveTo(x, y);
                            return 3;
                        case "attack":
                            unitAttack(x, y);
                            return 3;
                        case "found_city":
                            unitFoundCity(nameCity);
                            return 3;
                        case "pillage":
                            unitPillage();
                            return 3;
                        case "skip":
                            skipUnitTask();
                            return 3;
                        case "delete":
                            unitDelete();
                            return 3;
                        case "cancel_mission":
                            unitCancelMission();
                            return 3;
                        case "remove":
                            if (!object.equals("init"))
                                unitRemoveFromTile(object);
                            return 3;
                        case "repair":
                            unitRepair();
                            return 3;
                        case "build":
                            if (object.equals("init"))
                                System.out.println("invalid command");
                            else
                                unitBuild(object);
                            return 3;
                        default:
                            System.out.println("invalid command");
                            return 3;
                    }
                default:
                    System.out.println("invalid command");
            }
            return 3;
        }
    }

    static class menuCommands implements Runnable {
        @Parameter(names = {"exit", "-x"},
                description = "exits the current menu")
        boolean exit = false;
        @Parameter(names = {"show-current", "-s"},
                description = "shows the naem of the current menu")
        boolean show = false;
        @Parameter(names = {"enter", "-e"},
                description = "enters the menu you select(if it was not forbidden to do so)")
        String nextMenu = "init";

        public int run(String name) {
            if (exit && !show && nextMenu.equals("init"))
                return 2;
            else if (show && nextMenu.equals("init"))
                System.out.println("Game Menu");
            else if (nextMenu.equals("technologies") || nextMenu.equals("t")) {
                if (GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities().size() == 0)
                    System.out.println("you need at least one city to enter the technology menu");
                else {
                    ChooseTechnology chooseTechnology = new ChooseTechnology();
                    chooseTechnology.printDetails();
                    chooseTechnology.run(scanner);
                }
            } else if (nextMenu.equals("city-production") || nextMenu.equals("cp")) {
                if (GameController.getSelectedCity() == null)
                    System.out.println("no city is selected");
                else if (GameController.getSelectedCity().getCivilization() != GameController.getCivilizations().get(GameController.getPlayerTurn()))
                    System.out.println("the selected city is not yours");
                else {
                    ProductionCityMenu productionCityMenu = new ProductionCityMenu();
                    productionCityMenu.printDetails();
                    productionCityMenu.run(scanner);
                }
            } else if (!nextMenu.equals("init"))
                System.out.println("menu navigation is not possible");
            else
                System.out.println("invalid command");
            return 3;
        }
    }

    static class mapCommands implements Runnable {
        @Parameter(names = {"print", "-p"},
                description = "prints out tha map")
        boolean print = false;
        @Parameter(names = {"show", "-s"},
                description = "shows the thing you choose")
        boolean show = false;
        @Parameter(names = {"--city", "-c"},
                description = "shows the place of the city on the map")
        String city = "init";
        @Parameter(names = {"--tilex", "-x"},
                description = "X of the entry tile")
        int x = -1989;
        @Parameter(names = {"--tiley", "-y"},
                description = "Y of the entry tile",
                arity = 1)
        int y = -1989;
        @Parameter(names = {"--direction", "-d"},
                description = "direction to move the screen",
                arity = 1)
        String dir = "init";
        @Parameter(names = {"move", "-m"},
                description = "this command is used to move the screen")
        boolean move = false;
        @Parameter(names = {"--amount", "-a"},
                description = "this command is used to get the needed amount",
                arity = 1)
        int amount = -1989;

        public int run(String name) {
            if (print && !move && !show && city.equals("init"))
                System.out.println(GameController.printMap());
            else if (show && !city.equals("init") && (x == -1989 && y == -1989) && !move)
                mapShowCityName(city);
            else if (show && !move && (x != -1989 && y != -1989)) {
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
            } else System.out.println("invalid command");
            return 3;
        }
    }

    static class infoCommands implements Runnable {
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
                    citiesList.run(scanner);
                    return 3;
                case "economic", "e":
                    infoEconomic();
                    return 3;
                case "demographic", "d":
                    infoDemographic();
                    return 3;
                case "military", "m":
                    printMilitaryOverview();
                    return 3;
                default:
                    System.out.println("invalid command");
            }
            return 3;
        }
    }

    public static void printMilitaryOverview() {
        ArrayList<Unit> units = GameController.getCivilizations().get(GameController.getPlayerTurn()).getUnits();
        for (Unit unit : units)
            printUnitInfo(unit);
        if (units.size() == 0)
            System.out.println("you don't have any units right now");
    }

    private static void printUnitInfo(Unit unit) {
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

    static class cheatCommands implements Runnable {
        @Parameter(names = {"--amount", "-a"},
                description = "this command is used to get the needed amount",
                arity = 1)
        int amount = -1989;
        @Parameter(names = {"--object", "-o"},
                description = "the entry object")
        String obj = "init";
        @Parameter(names = {"--name", "-n"},
                description = "the name of the entry")
        String nameCity = "init";
        @Parameter(names = {"production", "-p"},
                description = "no caption needed")
        boolean production = false;
        @Parameter(names = {"science", "-s"},
                description = "no caption needed")
        boolean science = false;
        @Parameter(names = {"road_everywhere", "-re"},
                description = "inserts a road on every tiles of the map")
        boolean roadEverywhere = false;
        @Parameter(names = {"moveit-moveit", "-mi"},
                description = "moves the selected unit without caring about errors")
        boolean moveit = false;
        @Parameter(names = {"open_map", "-op"},
                description = "opens up to you every tile of the map without having to go to them")
        boolean openMap = false;
        @Parameter(names = {"take_city", "-tc"},
                description = "it's like taking photos, but with cities")
        boolean takeCity = false;
        @Parameter(names = {"technology", "-t"},
                description = "technology is the thing the you develop to become dumber(even more than before)")
        boolean technology = false;
        @Parameter(names = {"resource", "-r"},
                description = "resource is the thing they say when someone does a source twice")
        boolean resource = false;
        @Parameter(names = {"create", "-c"},
                description = "creates a unit selected by the you")
        String unit = "init";
        @Parameter(names = {"--tilex", "-x"},
                description = "X of the entry tile",
                arity = 1)
        int x = -1989;
        @Parameter(names = {"--tiley", "-y"},
                description = "Y of the entry tile",
                arity = 1)
        int y = -1989;

        public int run(String name) {
            if (science && amount != -1989)
                cheatScience(amount);
            else if (production && amount != -1989)
                cheatProduction(amount);
            else if (technology && !obj.equals("init"))
                cheatTechnology(TechnologyType.stringToEnum(obj));
            else if (resource && amount != -1989 && !obj.equals("init"))
                cheatResource(amount, ResourcesTypes.stringToEnum(obj));
            else if (moveit && x != 1989 && y != 1989)
                cheatMoveIt(x, y);
            else if (takeCity && !nameCity.equals("init"))
                cheatCaptureCity(nameCity);
            else if (openMap)
                GameController.openMap();
            else if (roadEverywhere)
                cheatRoadEverywhere();
            else if (!unit.equals("init") && (x != -1989 && y != -1989) && !obj.equals("init")) {
                cheatUnit(x, y, obj);
            } else
                System.out.println("invalid command");
            production = false;
            science = false;
            resource = false;
            technology = false;
            openMap = false;
            amount = -1989;
            unit = "init";
            x = -1989;
            y = -1989;
            obj = "init";
            return 3;
        }
    }

    static class cityCommands implements Runnable {
        @Parameter(names = {"show", "-s"},
                description = "shows the details of the city")
        String show = "init";
        @Parameter(names = {"start", "-st"},
                description = "starts producing the thing you choose")
        String startProducing = "init";
        @Parameter(names = {"--type", "-t"},
                description = "the type of entry")
        String type = "init";
        @Parameter(names = {"buy", "-b"},
                description = "buys the thing you choose")
        String buy = "init";
        @Parameter(names = {"citizen", "-c"},
                description = "assigns to citizen to the entry tile")
        String citizen = "init";
        @Parameter(names = {"build", "-bd"},
                description = "makes the builder build")
        String build = "init";
        @Parameter(names = {"attack", "-a"},
                description = "the act of assaulting someone")
        boolean attack = false;
        @Parameter(names = {"burn", "-bn"},
                description = "it burns")
        boolean burn = false;
        @Parameter(names = {"unit", "-u"},
                description = "is only used to buy unit")
        String unit = "init";
        @Parameter(names = {"capture", "-ca"},
                description = "the act of taking someone else's personal items")
        boolean capture = false;

        @Parameter(names = {"--destinationx", "-dx"},
                description = "X of the entry destination",
                arity = 1)
        int dx = -1989;
        @Parameter(names = {"--destinationy", "-dy"},
                description = "Y of the entry destination",
                arity = 1)
        int dy = -1989;
        @Parameter(names = {"--tilex", "-x"},
                description = "X of the entry",
                arity = 1)
        int ox = -1989;
        @Parameter(names = {"--tiley", "-y"},
                description = "X of the entry",
                arity = 1)
        int oy = -1989;

        public int run(String name) {
            if (!show.equals("init"))
                showBanner();
            else if (startProducing.equals("producing") && !type.equals("init"))
                startProducingUnit(type);
            else if ((buy.equals("tile") || buy.equals("t") && (ox != -1989 && oy != -1989)))
                buyTile(ox, oy);
            else if ((buy.equals("unit") || buy.equals("u")) && (!unit.equals("init")) && ox != -1989 && oy != -1989)
                buyUnit(unit, ox, oy);
            else if (citizen.equals("work") && (dx != -1989 && dy != -1989))
                assignCitizen(ox, oy, dx, dy);
            else if (burn)
                cityDestiny(true);
            else if (capture)
                cityDestiny(false);
            else if (attack && (dx != -1989 && dy != -1989))
                cityAttack(dx, dy);
            else if (build.equals("wall"))
                buildWall();
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

    public static void infoEconomic() {
        for (City city : GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities())
            CitiesList.cityBanner(city);
    }

    private static void cheatCaptureCity(String name) {
        switch (GameController.cheatCaptureCity(name)) {
            case 0:
                System.out.println("city captured successfully");
                break;
            case 1:
                System.out.println("no city with this name exists");
                break;
            case 2:
                System.out.println("this city is not yours");
                break;
        }
    }

    private static void cheatTechnology(TechnologyType technologyType) {
        switch (GameController.cheatTechnology(technologyType)) {
            case 0:
                System.out.println("cheat activated successfully");
                break;
            case 1:
                System.out.println("no technology with this name exists");
                break;
            case 2:
                System.out.println("you already have this technology");
                break;
        }
    }

    private static void cheatProduction(int amount) {
        switch (GameController.cheatProduction(amount)) {
            case 0:
                System.out.println("cheat activated successfully");
                break;
            case 1:
                System.out.println("no city is selected");
                break;
            case 2:
                System.out.println("the selected city is not yours");
                break;
        }
    }

    private static void cheatMoveIt(int x, int y) {
        switch (GameController.cheatMoveIt(x, y)) {
            case 0:
                System.out.println("cheat activated successfully");
                break;
            case 1:
                System.out.println("no unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
            case 3:
                System.out.println("invalid coordinates");
                break;
        }
    }

    private static void cheatScience(int amount) {
        GameController.cheatScience(amount);
        System.out.println("cheat activated successfully");
    }

    private static void cheatResource(int amount, ResourcesTypes resourcesTypes) {
        GameController.cheatResource(amount, resourcesTypes);
        System.out.println("cheat activated successfully");
    }

    private static void unitSleep() {
        switch (GameController.unitSleep()) {
            case 0:
                System.out.println("the selected unit has been set to sleep successfully");
                break;
            case 1:
                System.out.println("no unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
        }
    }

    private static void unitAlert() {
        switch (GameController.unitAlert()) {
            case 0:
                System.out.println("the selected unit has been set to alert successfully");
                break;
            case 1:
                System.out.println("no unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
            case 3:
                System.out.println("you can't go to alert position when there is enemy around");
                break;
        }
    }

    private static void unitFortify(boolean untilFullHealth) {
        int boolToInt = 0;
        if(untilFullHealth)
            boolToInt=1;
        switch (GameController.unitChangeState(boolToInt)) {
            case 0:
                if(untilFullHealth)
                    System.out.println("the selected unit has been set to fortifyUntilFullHealth successfully");
                else
                    System.out.println("the selected unit has been set to fortify successfully");
                break;
            case 1:
                System.out.println("no unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
            case 3:
                System.out.println("the selected unit is a civilian");
                break;
        }
    }

    private static void unitGarrison() {
        switch (GameController.unitChangeState(2)) {
            case 0:
                System.out.println("the selected unit has been set to garrison successfully");
                break;
            case 1:
                System.out.println("no unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
            case 3:
                System.out.println("the selected unit is a civilian");
                break;
        }
    }

    private static void unitWake() {
        switch (GameController.unitChangeState(3)) {
            case 0:
                System.out.println("the selected unit has been awaken successfully");
                break;
            case 1:
                System.out.println("no unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
            case 3:
                System.out.println("the selected unit is already awake");
                break;
        }
    }

    private static void unitSetupRanged() {
        switch (GameController.unitSetupRanged()) {
            case 0:
                System.out.println("the selected unit started setup successfully");
                break;
            case 1:
                System.out.println("no unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
            case 3:
                System.out.println("you can't setup this unit");
                break;
        }
    }

    private static void unitMoveTo(int x, int y) {
        if (x == -1989 || y == -1989)
            System.out.println("This command needs x and y");
        else {
            if (GameController.unitMoveTo(x, y))
                System.out.println("Moved successfully");
            else
                System.out.println("Moving failed");
        }
    }

    private static void unitAttack(int x, int y) {
        if (x == -1989 || y == -1989) System.out.println("This command needs x and y");
        else
            switch (GameController.unitAttack(x, y)) {
                case 0:
                    System.out.println("Attacked successfully");
                    break;
                case 1:
                    System.out.println("no unit is selected");
                    break;
                case 2:
                    System.out.println("the selected unit is not yours");
                    break;
                case 3:
                    System.out.println("the selected unit is not a nonCivilian");
                    break;
                case 4:
                    System.out.println("the entered tile is not valid");
                    break;
                case 5:
                    System.out.println("there is nothing to attack to");
                    break;
                case 6:
                    System.out.println("can't attack");
                    break;
                case 7:
                    System.out.println("need setup");
                    break;
                case 8:
                    System.out.println("already attacked");
                    break;
            }
    }

    private static void unitFoundCity(String name) {
        if (name.equals("init")) {
            System.out.println("please insert a name");
            return;
        }
        switch (GameController.unitFoundCity(name)) {
            case 0:
                System.out.println("city founded successfully");
                break;
            case 1:
                System.out.println("not unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
            case 3:
                System.out.println("the selected unit is not a settler");
                break;
            case 4:
                System.out.println("the selected tile is occupied with another city");
                break;
            case 5:
                System.out.println("the entered name is used already");
                break;
        }
    }

    private static void unitPillage() {
        switch (GameController.unitPillage()) {
            case 4:
                System.out.println("Select your unit first");
                break;
            case 3:
                System.out.println("Nothing to pillage");
                break;
            case 0:
                System.out.println("Pillaged successfully");
                break;
        }
    }

    private static void skipUnitTask() {
        switch (GameController.skipUnitTask()) {
            case 0:
                System.out.println("task skipped successfully");
                break;
            case 1:
                System.out.println("no unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
            case 3:
                System.out.println("the selected unit does not need a task");
                break;
        }
    }

    private static void unitDelete() {
        switch (GameController.unitDelete(GameController.getSelectedUnit())) {
            case 0:
                System.out.println("the selected unit has been deleted successfully");
                break;
            case 1:
                System.out.println("no unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
        }
    }

    private static void unitCancelMission() {
        switch (GameController.unitCancelMission()) {
            case 0:
                System.out.println("mission cancelled successfully");
                break;
            case 1:
                System.out.println("not unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
            case 3:
                System.out.println("the selected unit has no missions");
                break;
        }
    }

    private static void unitRemoveFromTile(String object) {
        boolean choose;
        if (object.equals("jungle")) choose = true;
        else if (object.equals("route")) choose = false;
        else return;
        switch (GameController.unitRemoveFromTile(choose)) {
            case 0:
                System.out.println(object + "'s removal from the tile operation started successfully");
                break;
            case 1:
                System.out.println("no unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
            case 3:
                System.out.println("the selected unit is not a worker");
                break;
            case 4:
                System.out.println("the selected tile does not have a jungle");
                break;
            case 5:
                System.out.println("the selected tile does not have any road or railroad");
                break;
        }
    }

    private static void unitRepair() {
        switch (GameController.unitRepair()) {
            case 0:
                System.out.println("improvement repaired successfully");
                break;
            case 1:
                System.out.println("no unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
            case 3:
                System.out.println("the selected unit is not a worker");
                break;
            case 4:
                System.out.println("there are no improvements here");
                break;
            case 5:
                System.out.println("the selected improvement does not need repairing");
                break;
        }
    }

    private static void unitBuild(String object) {
        ImprovementType improvementType = ImprovementType.stringToImprovementType(object);
        int result;
        if (improvementType != null)
            result = GameController.unitBuild(improvementType);
        else if (object.equals("road"))
            result = GameController.unitBuildRoad();
        else if (object.equals("railroad"))
            result = GameController.unitBuildRailRoad();
        else {
            System.out.println("no improvement with that name exists");
            return;
        }
        if (result == 0)
            System.out.println(object + "'s production started successfully");
        if (result == 1)
            System.out.println("no unit is selected");
        if (result == 2)
            System.out.println("the selected unit is not yours");
        if (result == 3)
            System.out.println("the selected unit is not a worker");
        if (result == 4)
            System.out.println("you don't have the prerequisite technologies");
        if (result == 5)
            System.out.println("this improvement cannot be inserted here");
        if (result == 6)
            System.out.println("you already have one here");
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
    }

    private static void infoUnits(boolean selected) {
        if (selected) {
            if (GameController.getSelectedUnit() == null)
                System.out.println("no unit selected");
            else
                printUnitInfo(GameController.getSelectedUnit());
        } else {
            UnitsList unitsList = new UnitsList();
            unitsList.printUnits();
            unitsList.run(scanner);
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

    private static void showBanner() {
        City city = GameController.getSelectedCity();
        if (city == null) {
            System.out.println("no city is selected");
            return;
        }
        System.out.println("name: " + city.getName());
        System.out.println("owner: " + city.getCivilization().getUser().getNickname());
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
            HashMap<ResourcesTypes, Integer> resourcesAmount = new HashMap<>();
            city.collectResources(resourcesAmount);
            System.out.println("resources: ");
            resourcesAmount.forEach((k, v) -> System.out.print(k + ": " + v));
        }

    }

    public static boolean startProducingUnit(String type) {
        switch (GameController.startProducingUnit(type)) {
            case 0:
                System.out.println("production started successfully");
                return true;
            case 1:
                System.out.println("no product with this name is defined");
                break;
            case 2:
                System.out.println("no city is selected");
                break;
            case 3:
                System.out.println("the selected city is not yours");
                break;
            case 4:
                System.out.println("you don't have enough money");
                break;
            case 5:
                System.out.println("you don't have the required resources");
                break;
            case 6:
                System.out.println("you don't have the required technology");
                break;
        }
        return false;
    }

    private static void buyTile(int ox, int oy) {
        switch (GameController.buyTile(ox, oy)) {
            case 0:
                System.out.println("Tile's been bought successfully");
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
                System.out.println("you don't have enough money");
                break;
        }
    }

    private static void assignCitizen(int ox, int oy, int dx, int dy) {
        int x;
        if ((ox != -1989 && oy != -1989))
            x = GameController.reAssignCitizen(ox, oy, dx, dy);
        else x = GameController.assignCitizen(dx, dy);
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

    private static void cityAttack(int dx, int dy) {
        switch (GameController.cityAttack(dx, dy)) {
            case 0:
                System.out.println("Attacked successfully");
                break;
            case 1:
                System.out.println("no city is selected");
                break;
            case 2:
                System.out.println("the selected city is not yours");
                break;
            case 3:
                System.out.println("the entered tile is not valid");
                break;
            case 4:
                System.out.println("no nonCivilian is standing there");
                break;
            case 5:
                System.out.println("you can't attack your own units");
                break;
            case 6:
                System.out.println("Can not attack there");
                break;
        }
    }

    private static void buildWall() {
        switch (GameController.buildWall()) {
            case 0:
                System.out.println("wall's production started successfully");
                break;
            case 1:
                System.out.println("no city is selected");
                break;
            case 2:
                System.out.println("the selected city is not yours");
                break;
            case 3:
                System.out.println("the selected city already has a wall");
                break;
        }
    }

    private static void mapShowCityName(String city) {
        switch (GameController.mapShowCityName(city)) {
            case 0:
                System.out.println("city selected successfully");
                break;
            case 1:
                System.out.println("no city with this name exists");
                break;
            case 2:
                System.out.println("you have not unlocked the position of this city yet");
                break;
        }
    }

    private static void buyUnit(String unit, int x, int y) {
        switch (GameController.buyUnit(unit, x, y)) {
            case 0:
                System.out.println("unit purchased successfully");
                break;
            case 1:
                System.out.println("no unit exists with this name");
                break;
            case 2:
                System.out.println("the given coordinate is not in your territory");
                break;
            case 3:
                System.out.println("you don't have enough money");
                break;
            case 4:
                System.out.println("the selected tile is occupied by another unit");
                break;
        }
    }

    private static void cheatUnit(int x, int y, String obj) {
        UnitType unitType = UnitType.stringToEnum(obj);
        if(unitType==null)
        {
            System.out.println("no unit with this name exists");
            return;
        }
        switch (GameController.cheatUnit(x, y, unitType)) {
            case 0:
                System.out.println("cheat activated successfully");
                break;
            case 1:
                System.out.println("the unit cannot be placed here");
                break;
            case 2:
                System.out.println("the selected tile is occupied by another unit");
                break;
        }
    }

    private static void cheatRoadEverywhere() {
        if (GameController.cheatRoadEverywhere() == 0)
            System.out.println("cheat activated successfully");
    }

    protected JCommander jCommander() {
        JCommander jCommander = new JCommander();
        jCommander.setAllowAbbreviatedOptions(false);
        jCommander.addCommand("city", new cityCommands());
        jCommander.addCommand("cheat", new cheatCommands());
        jCommander.addCommand("info", new infoCommands());
        jCommander.addCommand("map", new mapCommands());
        jCommander.addCommand("menu", new menuCommands());
        jCommander.addCommand("unit", new unitState());
        jCommander.addCommand("increase", new increase());
        jCommander.addCommand("select", new tileXAndYFlagSelectUnit());
        jCommander.addCommand("next-turn", new FreeFlagCommands());
        return jCommander;
    }
}
