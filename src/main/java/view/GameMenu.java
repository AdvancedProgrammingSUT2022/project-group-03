package view;

import controller.GameController;
import model.Units.NonCivilian;
import model.Units.Settler;

import java.util.regex.Matcher;

public class GameMenu extends Menu {

    private void infoResearch() {

    }

    private void infoUnits() {

    }


    private void infoCities() {

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
        if (GameController.setSelectedCombatUnit(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))))
            System.out.println("combat unit selected successfully");
        else
            System.out.println("selection failed");
    }

    private void selectUnitNonCombatUnit(String command) {
        Matcher matcher = getMatcher(regexes[4], command);
        if (GameController.setSelectedNonCombatUnit(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))))
            System.out.println("noncombat unit selected successfully");
        else
            System.out.println("selection failed");
    }

    private void selectCityByName(String command) {

    }


    private void selectCityByPosition(String command) {

    }

    private void unitMoveTo(String command) {
        Matcher matcher = getMatcher(regexes[2], command);
        if (GameController.UnitMoveTo(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))))
            System.out.println("Moved successfully");
        else
            System.out.println("Moving failed");
    }

    private void unitSleep() {

    }

    private void unitAlert() {

    }

    private void unitFortify() {

    }

    private void unitGarrison() {

    }

    private void unitSetupRanged() {

    }

    private void unitAttack(String command) {

    }

    private void unitFoundCity() {
        int result = GameController.UnitFoundCity();
        if(result==0)
            System.out.println("city founded successfully");
        if(result==1)
            System.out.println("the selected unit is not a settler");
        if(result==2)
            System.out.println("the selected tile is occupied with another city");
    }

    private void unitCancelMission() {

    }

    private void unitWake() {

    }

    private void unitDelete() {

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

    }


    private void mapShowCityName(String command) {

    }

    private void selectedUnitInfo()
    {
        if(GameController.getSelectedUnit()==null)
            System.out.println("no unit selected");
        else
            System.out.println("MP: " + GameController.getSelectedUnit().getMovementPrice());
    }
    private void mapMove(String command) {
        Matcher matcher = getMatcher(regexes[6], command);
        if(matcher.group(2)==null)
            GameController.mapMove(1,matcher.group(1));
        else
            GameController.mapMove(Integer.parseInt(matcher.group(2).replace(" ", "")),matcher.group(1));
        System.out.println("map moved successfully");
    }

    private void startProducing(String command)
    {
        Matcher matcher = getMatcher(regexes[10],command);
        GameController.startProducing(matcher.group(1));
    }

    private void nextTurn() {
        if (GameController.nextTurn())
            System.out.println("turn ended successfully");
        else
            System.out.println("failed to end the turn");
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
                "^START PRODUCING (\\w+)$"

        };
    }
    private void cheatSettler(String command)
    {
        Matcher matcher = getMatcher(regexes[7], command);
        GameController.cheatSettler(Integer.parseInt(matcher.group(1)),Integer.parseInt(matcher.group(2)));
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
        }
        System.out.println(GameController.printMap());
        return false;
    }


    protected boolean gameOver() {
        return false;
    }
}


