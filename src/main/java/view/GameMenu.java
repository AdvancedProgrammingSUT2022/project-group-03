package view;

import controller.GameController;

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


    private void MapShowPosition(String command) {

    }


    private void MapShowCityName(String command) {

    }


    private void MapMoveRight(String command) {

    }

    private void MapMoveLeft(String command) {

    }


    private void MapMoveUp(String command) {

    }


    private void MapMoveDown(String command) {

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
                "^NEXT TURN$"
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
        }
        return false;
    }

    protected boolean gameOver() {
        return false;
    }
}


