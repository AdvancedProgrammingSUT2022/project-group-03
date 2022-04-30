package view;

import controller.GameController;
import model.City;
import model.Units.Unit;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class GameMenu extends Menu {

    private void infoResearch() {
        ArrayList<City> cities = GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities();
        boolean doWeHaveAnyWorkingTechnology = false;
        for (City city : cities) {
            if(city.getProduct()==null)
                continue;
            doWeHaveAnyWorkingTechnology = true;
            String tempString = null;
            if (city.getProduct() instanceof Unit)
                tempString = ((Unit) city.getProduct()).getUnitType().toString();
            System.out.print(city.getName() + ": " + "getting developed technology: " + tempString);
        }
        if(!doWeHaveAnyWorkingTechnology)
            System.out.println("you don't have any technology in the development right now");
    }

    private void infoUnits() {
        ArrayList<Unit> units = GameController.getCivilizations().get(GameController.getPlayerTurn()).getUnits();
        for (Unit unit : units) {
            System.out.print(unit.getUnitType() +
                    "| Health: " + unit.getHealth() +
                    "| currentX: " + unit.getCurrentTile().getX() +
                    " currentY: " + unit.getCurrentTile().getY());
            if (unit.getDestinationTile() != null)
                System.out.print("| destinationX: " + unit.getDestinationTile().getX()
                        + " destinationY: " + unit.getDestinationTile().getY());
        }
        if(units.size()==0)
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
                System.out.print("Yes");
            else System.out.print("No");


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
        if (!GameController.setSelectedCityByName(command))
            System.out.println("city does not exist");
    }


    private void selectCityByPosition(String command) {
        Matcher matcher = getMatcher(regexes[15], command);
        if (!GameController.setSelectedCityByPosition(Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2))))
            System.out.println("city does not exist");

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
        if(result==0)
            System.out.println("the selected unit has been set to alert successfully");
        if(result==1)
            System.out.println("no unit is selected");
        if(result==2)
            System.out.println("the selected unit is not yours");
    }

    private void unitFortify() {
        int result = GameController.unitFortify();
        if(result==0)
            System.out.println("the selected unit has been set to fortify successfully");
        if(result==1)
            System.out.println("no unit is selected");
        if(result==2)
            System.out.println("the selected unit is not yours");
    }
    private void unitFortifyUntilFullHealth() {
        int result = GameController.unitFortifyUntilFullHealth();
        if(result==0)
            System.out.println("the selected unit has been set to fortifyUntilFullHealth successfully");
        if(result==1)
            System.out.println("no unit is selected");
        if(result==2)
            System.out.println("the selected unit is not yours");
    }

    private void unitGarrison() {
        int result = GameController.unitGarrison();
        if(result==0)
            System.out.println("the selected unit has been set to garrison successfully");
        if(result==1)
            System.out.println("no unit is selected");
        if(result==2)
            System.out.println("the selected unit is not yours");
    }

    private void unitSetupRanged() {

    }

    private void unitAttack(String command) {

    }

    private void unitFoundCity() {
        int result = GameController.unitFoundCity();
        if (result == 0)
            System.out.println("city founded successfully");
        if(result==1)
            System.out.println("not unit is selected");
        if (result == 2)
            System.out.println("the selected unit is not yours");
        if (result == 3)
            System.out.println("the selected unit is not a settler");
        if (result == 4)
            System.out.println("the selected tile is occupied with another city");
    }

    private void unitCancelMission() {

    }

    private void unitWake() {
        int result = GameController.unitWake();
        if (result == 0)
            System.out.println("the selected unit has been awaken successfully");
        if(result==1)
            System.out.println("no unit is selected");
        if (result == 2)
            System.out.println("the selected unit is not yours");
        if (result == 3)
            System.out.println("the selected unit is already awake");
    }

    private void unitDelete() {
        int result = GameController.unitDelete(GameController.getSelectedUnit());
        if(result==0)
            System.out.println("the selected unit has been deleted successfully");
        if(result==1)
            System.out.println("no unit is selected");
        if (result == 2)
            System.out.println("the selected unit is not yours");

    }

    private void unitBuildRoad() {

    }

    private void unitBuildRailroad() {

    }

    private void unitBuildFarm() {

    }

    private void unitBuildMine() {

    }

    private void unitBuildTradingPost() {

    }

    private void unitBuildLumbermill() {

    }

    private void unitBuildPasture() {

    }

    private void unitBuildCamp() {

    }

    private void unitBuildPlantation() {

    }

    private void unitBuildQuarry() {

    }

    private void unitRemove() {

    }

    private void unitRepair() {

    }


    private void mapShowPosition(String command) {
        Matcher matcher = getMatcher(regexes[16], command);
        if (!GameController.mapShowPosition(Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2))))
            System.out.println("invalid position");
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
    }

    private void selectedUnitInfo() {
        if (GameController.getSelectedUnit() == null)
            System.out.println("no unit selected");
        else
            System.out.println("MP: " + GameController.getSelectedUnit().getMovementPrice());
    }

    private void mapMove(String command) {
        Matcher matcher = getMatcher(regexes[6], command);
        boolean result;
        if (matcher.group(2) == null)
            result = GameController.mapMove(1, matcher.group(1));
        else
            result = GameController.
                    mapMove(Integer.parseInt(matcher.group(2).replace(" ", "")),
                            matcher.group(1));
        if (result)
            System.out.println("map moved successfully");
        else System.out.println("Invalid position");
    }

    private void startProducing(String command) {
        Matcher matcher = getMatcher(regexes[10], command);
        GameController.startProducing(matcher.group(1));
    }

    private void technologyMenu() {
        if(GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities().size()==0)
            System.out.println("you need at least one city to enter the technology menu");
        else
        {
            ChooseTechnology chooseTechnology = new ChooseTechnology();
            chooseTechnology.printDetails();
            chooseTechnology.run(scanner);
        }

    }

    private void cityProductionMenu() {
        ProductionCityMenu productionCityMenu = new ProductionCityMenu();
        productionCityMenu.run(scanner);
    }

    private void nextTurn() {
        if (GameController.nextTurnIfYouCan())
            System.out.println("turn ended successfully");
        else
        {
            System.out.print("failed to end the turn: " + GameController.getUnfinishedTasks().get(0).getTaskTypes());
            if(GameController.getUnfinishedTasks().get(0).getTile()==null)
                System.out.println();
            else System.out.println(" | x: " + GameController.getUnfinishedTasks().get(0).getTile().getX() +
                    " y: " + GameController.getUnfinishedTasks().get(0).getTile().getY());
        }
    }

    private void cheatSettler(String command) {
        Matcher matcher = getMatcher(regexes[7], command);
        GameController.cheatSettler(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
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
            System.out.print("\nHP: " + city.getHP());
            System.out.println("strength: " + city.getStrength());
        }

    }

    private void increaseTurn(String command) {
        Matcher matcher = getMatcher(regexes[21], command);
        for (int i = 0; i < Integer.parseInt(matcher.group(1)) * GameController.getCivilizations().size(); i++)
            GameController.nextTurn();
    }

    private void increaseGold(String command) {
        Matcher matcher = getMatcher(regexes[22], command);
        GameController.getCivilizations().get(GameController.getPlayerTurn())
                .increaseGold(Integer.parseInt(matcher.group(1)));
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
                "^CHEAT CREATE SETTLER ([0-9]+) ([0-9]+)",
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
                "^UNIT DELETE$",
                "^PRINT MAP$"
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
                cheatSettler(command);
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
        }

        return false;
    }


    protected boolean gameOver() {
        return false;
    }
}


