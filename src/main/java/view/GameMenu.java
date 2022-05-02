package view;

import controller.GameController;
import model.City;
import model.Map;
import model.Units.Unit;
import model.Units.UnitType;
import model.improvements.ImprovementType;
import model.resources.ResourcesTypes;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;

public class GameMenu extends Menu {

    private void infoResearch() {
        ArrayList<City> cities = GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities();
        boolean doWeHaveAnyWorkingTechnology = false;
        for (City city : cities) {
            if (city.getProduct() == null)
                continue;
            doWeHaveAnyWorkingTechnology = true;
            String tempString = null;
            if (city.getProduct() instanceof Unit)
            {
                tempString = ((Unit) city.getProduct()).getUnitType().toString() + ": (";
                int cyclesToComplete = city.cyclesToComplete(city.getProduct().getRemainedCost());
                if(cyclesToComplete==12345)
                    tempString += "never, your production is 0)";
                else
                    tempString+= city.cyclesToComplete(city.getProduct().getRemainedCost()) + " days to complete)";
            }
            System.out.print(city.getName() + ": " + "Being developed technology: " + tempString);
        }
        if (!doWeHaveAnyWorkingTechnology)
            System.out.println("you don't have any technology in the development right now");
    }

    private void infoUnits() {
        ArrayList<Unit> units = GameController.getCivilizations().get(GameController.getPlayerTurn()).getUnits();
        for (Unit unit : units)
            printUnitInfo(unit);
        if (units.size() == 0)
            System.out.println("you don't have any units right now");
    }


    private void infoCities() {
        ArrayList<City> cities = GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities();
        for (City city : cities) {
            System.out.print(city.getName() +
                    ": | strength: " + city.getStrength() +
                    " | mainTileX: " + city.getMainTile().getX() +
                    " | mainTileY: " + city.getMainTile().getY() +
                    " | population: " + city.getPopulation() +
                    " | food: " + city.getFood() +
                    " | citizen: " + city.getCitizen() +
                    " | founder: " + city.getFounder().getUser().getNickname() +
                    " | doesHaveWall: ");
            if (city.getDoesHaveWall())
                System.out.println("Yes");
            else System.out.println("No");


        }
    }

    private void infoDiplomacy() {

    }


    private void infoVictory() {

    }


    private void infoDemographics() {

    }


    private void infoNotifications() {

    }


    private void infoMilitary() {

    }


    private void infoEconomic() {

    }


    private void infoDiplomatic() {

    }

    private void infoDeals() {

    }


    private void selectUnitCombatUnit(String command) {
        Matcher matcher = getMatcher(regexes[3], command);
        if (GameController.setSelectedCombatUnit(Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2))))
            System.out.println("combat unit selected successfully");
        else
            System.out.println("selection failed");
    }

    private void selectUnitNonCombatUnit(String command) {
        Matcher matcher = getMatcher(regexes[4], command);
        if (GameController.setSelectedNonCombatUnit(Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2))))
            System.out.println("noncombat unit selected successfully");
        else
            System.out.println("selection failed");
    }

    private void selectCityByName(String command) {
        Matcher matcher = getMatcher(regexes[14], command);
        if (!GameController.setSelectedCityByName(matcher.group(1)))
            System.out.println("city does not exist");
        else
            System.out.println("city selected successfully");
    }


    private void selectCityByPosition(String command) {
        Matcher matcher = getMatcher(regexes[15], command);
        if (!GameController.setSelectedCityByPosition(Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2))))
            System.out.println("city does not exist");
        else
            System.out.println("city selected successfully");
    }

    private void unitMoveTo(String command) {
        Matcher matcher = getMatcher(regexes[2], command);
        if (GameController.unitMoveTo(Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2))))
            System.out.println("Moved successfully");
        else
            System.out.println("Moving failed");
    }

    private void unitSleep() {
        int result = GameController.unitSleep();
        if (result == 0)
            System.out.println("the selected unit has been set to sleep successfully");
        if (result == 1)
            System.out.println("no unit is selected");
        if (result == 2)
            System.out.println("the selected unit is not yours");

    }

    private void unitAlert() {
        int result = GameController.unitAlert();
        if (result == 0)
            System.out.println("the selected unit has been set to alert successfully");
        if (result == 1)
            System.out.println("no unit is selected");
        if (result == 2)
            System.out.println("the selected unit is not yours");
        if (result == 3)
            System.out.println("you can't go to alert position when there is enemy around");
    }

    private void unitFortify() {
        int result = GameController.unitFortify();
        if (result == 0)
            System.out.println("the selected unit has been set to fortify successfully");
        if (result == 1)
            System.out.println("no unit is selected");
        if (result == 2)
            System.out.println("the selected unit is not yours");
    }

    private void unitFortifyUntilFullHealth() {
        int result = GameController.unitFortifyUntilFullHealth();
        if (result == 0)
            System.out.println("the selected unit has been set to fortifyUntilFullHealth successfully");
        if (result == 1)
            System.out.println("no unit is selected");
        if (result == 2)
            System.out.println("the selected unit is not yours");
    }

    private void unitGarrison() {
        int result = GameController.unitGarrison();
        if (result == 0)
            System.out.println("the selected unit has been set to garrison successfully");
        if (result == 1)
            System.out.println("no unit is selected");
        if (result == 2)
            System.out.println("the selected unit is not yours");
    }

    private void unitSetupRanged() {

    }

    private void unitAttack(String command) {
        Matcher matcher = getMatcher(regexes[35], command);
        switch (GameController.unitAttack(Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)))) {
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
            case 4:
                System.out.println("Can't find a route");
        }

    }

    private void unitFoundCity() {
        int result = GameController.unitFoundCity();
        if (result == 0)
            System.out.println("city founded successfully");
        if (result == 1)
            System.out.println("not unit is selected");
        if (result == 2)
            System.out.println("the selected unit is not yours");
        if (result == 3)
            System.out.println("the selected unit is not a settler");
        if (result == 4)
            System.out.println("the selected tile is occupied with another city");
    }

    private void unitCancelMission() {
        int result = GameController.unitCancelMission();
        if (result == 0)
            System.out.println("mission cancelled successfully");
        if (result == 1)
            System.out.println("not unit is selected");
        if (result == 2)
            System.out.println("the selected unit is not yours");
        if (result == 3)
            System.out.println("the selected unit has no missions");
    }

    private void unitWake() {
        int result = GameController.unitWake();
        if (result == 0)
            System.out.println("the selected unit has been awaken successfully");
        if (result == 1)
            System.out.println("no unit is selected");
        if (result == 2)
            System.out.println("the selected unit is not yours");
        if (result == 3)
            System.out.println("the selected unit is already awake");
    }

    private void unitDelete() {
        int result = GameController.unitDelete(GameController.getSelectedUnit());
        if (result == 0)
            System.out.println("the selected unit has been deleted successfully");
        if (result == 1)
            System.out.println("no unit is selected");
        if (result == 2)
            System.out.println("the selected unit is not yours");

    }

    private void unitRemove(String command) {
        Matcher matcher = getMatcher(regexes[33], command);
        boolean isJungle = false;
        if (matcher.group(1).equals("jungle"))
            isJungle = true;
        else if (!matcher.group(1).equals("route")) {
            System.out.println("invalid command");
            return;
        }
        int result = GameController.unitRemoveFromTile(isJungle);
        if (result == 0)
            System.out.println(matcher.group(1) + "'s removal from the tile operation started successfully");
        if (result == 1)
            System.out.println("no unit is selected");
        if (result == 2)
            System.out.println("the selected unit is not yours");
        if (result == 3)
            System.out.println("the selected unit is not a worker");
        if (result == 4)
            System.out.println("the selected tile does not have a jungle");
    }

    private void unitRepair() {
        int result = GameController.unitRepair();
        if (result == 0)
            System.out.println("improvement repaired successfully");
        if (result == 1)
            System.out.println("no unit is selected");
        if (result == 2)
            System.out.println("the selected unit is not yours");
        if (result == 3)
            System.out.println("the selected unit is not a worker");
        if (result == 4)
            System.out.println("there are no improvements here");
        if (result == 5)
            System.out.println("the selected improvement does not need repairing");
    }


    private void mapShowPosition(String command) {
        Matcher matcher = getMatcher(regexes[16], command);
        GameController.mapShowPosition(Integer.parseInt(matcher.group(1)) - Map.WINDOW_X / 2,
                Integer.parseInt(matcher.group(2)) - Map.WINDOW_Y / 2 + 1);
        System.out.println(GameController.printMap());
    }


    private void mapShowCityName(String command) {
        Matcher matcher = getMatcher(regexes[17], command);
        int result = GameController.mapShowCityName(matcher.group(1));
        if (result == 1)
            System.out.println("no city with this name exists");
        if (result == 2)
            System.out.println("you have not unlocked the position of this city yet");
        if (result == 0)
            System.out.println("city selected successfully");
        System.out.println(GameController.printMap());
    }

    private void printUnitInfo(Unit unit) {
        System.out.print(unit.getUnitType() +
                ": | Health: " + unit.getHealth() +
                " | currentX: " + unit.getCurrentTile().getX() +
                " | currentY: " + unit.getCurrentTile().getY());
        if (unit.getDestinationTile() != null)
            System.out.print(" | destinationX: " + unit.getDestinationTile().getX()
                    + " destinationY: " + unit.getDestinationTile().getY());
    }

    private void selectedUnitInfo() {
        if (GameController.getSelectedUnit() == null)
            System.out.println("no unit selected");
        else
            printUnitInfo(GameController.getSelectedUnit());
    }

    private void mapMove(String command) {
        Matcher matcher = getMatcher(regexes[6], command);
        if (matcher.group(2) == null)
            GameController.mapMove(1, matcher.group(1));
        else
            GameController.
                    mapMove(Integer.parseInt(matcher.group(2).replace(" ", "")),
                            matcher.group(1));
        System.out.println("map moved successfully");
    }

    private void startProducing(String command) {
        Matcher matcher = getMatcher(regexes[10], command);
        int result = GameController.startProducing(matcher.group(1));
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

    private void technologyMenu() {
        if (GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities().size() == 0)
            System.out.println("you need at least one city to enter the technology menu");
        else {
            ChooseTechnology chooseTechnology = new ChooseTechnology();
            chooseTechnology.printDetails();
            chooseTechnology.run(scanner);
        }

    }

    private void cityProductionMenu() {
        if (GameController.getSelectedCity() == null)
            System.out.println("no city is selected");
        else if (GameController.getSelectedCity().getCivilization() != GameController.getCivilizations().get(GameController.getPlayerTurn()))
            System.out.println("the selected city is not yours");
        else {
            ProductionCityMenu productionCityMenu = new ProductionCityMenu();
            productionCityMenu.printDetails();
            productionCityMenu.run(scanner);
        }
    }

    private void nextTurn() {
        if (GameController.nextTurnIfYouCan())
            System.out.println("turn ended successfully");
        else {
            System.out.print("failed to end the turn: " + GameController.getUnfinishedTasks().get(0).getTaskTypes());
            if (GameController.getUnfinishedTasks().get(0).getTile() == null)
                System.out.println();
            else System.out.println(" | x: " + GameController.getUnfinishedTasks().get(0).getTile().getX() +
                    " y: " + GameController.getUnfinishedTasks().get(0).getTile().getY());
        }
    }

    private void cheatUnit(String command) {
        Matcher matcher = getMatcher(regexes[7], command);
        UnitType unitType = UnitType.stringToEnum(matcher.group(1));
        GameController.cheatUnit(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)), unitType);
    }

    private void cityShowDetails() {
        City city = GameController.getSelectedCity();
        if (city == null) {
            System.out.println("no city is selected");
            return;
        }
        System.out.println("name: " + city.getName());
        System.out.println("founder: " + city.getFounder().getUser().getNickname());
        if (city.getCivilization() == GameController.getCivilizations().get(GameController.getPlayerTurn())) {
            System.out.println("gold: " + city.getGold());
            System.out.println("production: " + city.getProduction());
            System.out.println("food: " + city.getFood());
            System.out.println("population: " + city.getPopulation());
            System.out.println("citizens: " + city.getCitizen());
            System.out.print("getting worked on tiles: ");
            for (int i = 0; i < city.getGettingWorkedOnByCitizensTiles().size(); i++)
                System.out.print(city.getGettingWorkedOnByCitizensTiles().get(i).getX() + "," +
                        city.getGettingWorkedOnByCitizensTiles().get(i).getY() + "   |   ");
            System.out.println("\nHP: " + city.getHP());
            System.out.println("strength: " + city.getStrength());
        }

    }

    private void increaseTurn(String command) {
        Matcher matcher = getMatcher(regexes[21], command);
        for (int i = 0; i < Integer.parseInt(matcher.group(1)) * GameController.getCivilizations().size(); i++)
            GameController.nextTurn();
        System.out.println("cheat activated successfully");
    }

    private void unitBuild(String command) {
        Matcher matcher = getMatcher(regexes[30], command);
        ImprovementType improvementType = ImprovementType.stringToImprovementType(matcher.group(1));
        int result;
        if (improvementType != null)
            result = GameController.unitBuild(improvementType);
        else if (matcher.group(1).toLowerCase(Locale.ROOT).equals("road"))
            result = GameController.unitBuildRoad();
        else if (matcher.group(1).toLowerCase(Locale.ROOT).equals("railroad"))
            result = GameController.unitBuildRailRoad();
        else {
            System.out.println("no improvement with that name exists");
            return;
        }
        if (result == 0)
            System.out.println(matcher.group(1) + "'s production started successfully");
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
    }

    private void cheatScience(String command)
    {
        Matcher matcher = getMatcher(regexes[36],command);
        GameController.cheatScience(Integer.parseInt(matcher.group(1)));
    }

    private void cheatProduction(String command)
    {
        Matcher matcher = getMatcher(regexes[37],command);
        GameController.cheatProduction(Integer.parseInt(matcher.group(1)));
    }
    private void cheatResource(String command)
    {
        Matcher matcher = getMatcher(regexes[38],command);
        GameController.cheatResource(Integer.parseInt(matcher.group(2)), ResourcesTypes.stringToEnum(matcher.group(1)));
    }

    private void increaseGold(String command) {
        Matcher matcher = getMatcher(regexes[22], command);
        GameController.getCivilizations().get(GameController.getPlayerTurn())
                .increaseGold(Integer.parseInt(matcher.group(1)));
        System.out.println("cheat activated successfully");
    }

    {
        regexes = new String[]{
                "^menu exit$",
                "^menu show-current$",
                "^UNIT MOVETO ([0-9]+) ([0-9]+)$",
                "^SELECT UNIT COMBAT ([0-9]+) ([0-9]+)$",
                "^SELECT UNIT NONCOMBAT ([0-9]+) ([0-9]+)$",
                "^NEXT TURN$",
                "^MAP MOVE (R|L|U|D)( [0-9]+)?",
                "^CHEAT CREATE UNIT (\\w+) ([0-9]+) ([0-9]+)",
                "^UNIT FOUND CITY$",
                "^SELECTED UNIT INFO$",
                "^START PRODUCING (\\w+)$",
                "^menu enter Technologies$",
                "^menu enter CityProduction$",
                "^city show details$", //13
                "^SELECT city (\\w+)$",
                "^SELECT city (\\d+) (\\d+)$",
                "^MAP SHOW (\\d+) (\\d+)$",
                "^MAP SHOW (\\w+)$",
                "^INFO RESEARCH$",
                "^INFO UNITS$",
                "^INFO CITIES$", //20
                "^increase --turn (\\d+)$",
                "^increase --gold (\\d+)$",
                "^UNIT SLEEP$",
                "^UNIT ALERT$",
                "^UNIT FORTIFY$",
                "^UNIT FORTIFY_UNTIL_HEAL$",
                "^UNIT GARRISON$",
                "^UNIT DELETE$", //28
                "^PRINT MAP$",
                "^build improvement (\\w+)$",
                "^unit repair$",
                "^unit cancel mission$",
                "^unit remove (\\w+)$", //33
                "^unit wake$",
                "^UNIT ATTACK ([0-9]+) ([0-9]+)$",
                "^CHEAT SCIENCE (\\d+)$",
                "^CHEAT PRODUCTION (\\d+)$",
                "^CHEAT RESOURCE (\\w+) (\\d+)$"
        };
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
                System.out.println("Game Menu");
                break;
            case 2:
                unitMoveTo(command);
                break;
            case 3:
                selectUnitCombatUnit(command);
                break;
            case 4:
                selectUnitNonCombatUnit(command);
                break;
            case 5:
                nextTurn();
                break;
            case 6:
                mapMove(command);
                break;
            case 7:
                cheatUnit(command);
                break;
            case 8:
                unitFoundCity();
                break;
            case 9:
                selectedUnitInfo();
                break;
            case 10:
                startProducing(command);
                break;
            case 11:
                technologyMenu();
                break;
            case 12:
                cityProductionMenu();
                break;
            case 13:
                cityShowDetails();
                break;
            case 14:
                selectCityByName(command);
                break;
            case 15:
                selectCityByPosition(command);
                break;
            case 16:
                mapShowPosition(command);
                break;
            case 17:
                mapShowCityName(command);
                break;
            case 18:
                infoResearch();
                break;
            case 19:
                infoUnits();
                break;
            case 20:
                infoCities();
                break;
            case 21:
                increaseTurn(command);
                break;
            case 22:
                increaseGold(command);
                break;
            case 23:
                unitSleep();
                break;
            case 24:
                unitAlert();
                break;
            case 25:
                unitFortify();
                break;
            case 26:
                unitFortifyUntilFullHealth();
                break;
            case 27:
                unitGarrison();
                break;
            case 28:
                unitDelete();
                break;
            case 29:
                System.out.println(GameController.printMap());
                break;
            case 30:
                unitBuild(command);
                break;
            case 31:
                unitRepair();
                break;
            case 32:
                unitCancelMission();
                break;
            case 33:
                unitRemove(command);
                break;
            case 34:
                unitWake();
                break;
            case 35:
                unitAttack(command);
                break;
            case 36:
                cheatScience(command);
                break;
            case 37:
                cheatProduction(command);
                break;
            case 38:
                cheatResource(command);
                break;
        }

        return false;
    }


    protected boolean gameOver() {
        return false;
    }
}


