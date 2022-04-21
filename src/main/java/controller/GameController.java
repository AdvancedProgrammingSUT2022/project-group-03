package controller;

import model.*;
import model.Units.Unit;
import model.tiles.Tile;

import java.util.ArrayList;

public class GameController {
    private static ArrayList<Civilization> civilizations = new ArrayList<>();
    private static Unit selectedUnit;
    private static City selectedCity;
    private static Map map;
    private static int startWindowX;
    private static int startWindowY;
    private static int PlayerTurn = 0;
    private static ArrayList<Tasks> unfinishedTasks = new ArrayList<>();

    public static void startGame(ArrayList<User> PlayersNames) {
        setCivilizations(PlayersNames);
        map = new Map(civilizations);
    }

    public static int setSelectedUnit(int x, int y, boolean isCitizen) {
        return 0;
    }

    public static int setSelectedCityByName(String name) {
        return 0;
    }


    public static int setSelectedCityByPosition(int x, int y) {
        return 0;
    }

    public static int UnitMoveTo(int x, int y) {
        return 0;
    }

    public static int UnitSleep() {
        return 0;
    }

    public static int UnitAlert() {
        return 0;
    }

    public static int UnitFortify(boolean shouldHeal) {
        return 0;
    }

    public static int UnitGarrison() {
        return 0;
    }

    public static int UnitSetupRanged() {
        return 0;
    }

    public static int UnitAttackPosition(int x, int y) {
        return 0;
    }

    public static int UnitFoundCity() {
        return 0;
    }

    public static int unitCancelMission() {
        return 0;
    }


    public static int UnitWake() {
        return 0;
    }


    public static int UnitDelete() {
        return 0;
    }


    public static int UnitBuildRoad() {
        return 0;
    }

    public static int UnitBuildRailRoad() {
        return 0;
    }

    public static int UnitRemoveFromTile(int isJungle) {
        return 0;
    }

    public static int UnitRepair() {
        return 0;
    }

    public static int mapShowPosition(int x, int y) {
        return 0;
    }


    public static int mapShowCityName(String name) {
        return 0;
    }

    public static int mapMove(int x, int y, String direction) {
        return 0;
    }

    private static int nextTurn() {
        return 0;
    }

    public static int buyTile(Tile tile) {
        return 0;
    }

    private static void setCivilizations(ArrayList<User> users) {
        for (int i = 0; i < users.size(); i++)
            civilizations.add(new Civilization(users.get(i)));
    }

    private static void setTheNumbers() {

    }

    private static boolean canUnitAttack(Unit unit, Tile tile) {
        return true;
    }

    private static boolean canCityAttack(City city, Tile tile) {
        return true;
    }

    private static boolean canUnitMove(City city, Tile tile) {
        return true;
    }

    private static int isGameOver() {
        return 0;
    }

    public static Map getMap() {
        return map;
    }
}
