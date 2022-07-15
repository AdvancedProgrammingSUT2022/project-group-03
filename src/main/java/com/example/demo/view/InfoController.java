package com.example.demo.view;

import com.example.demo.controller.TechnologyAndProductionController;
import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.City;
import com.example.demo.model.Civilization;
import com.example.demo.model.Units.NonCivilian;
import com.example.demo.model.Units.Unit;
import com.example.demo.model.Units.UnitState;
import com.example.demo.model.Units.UnitType;
import com.example.demo.model.building.BuildingType;
import com.example.demo.model.improvements.ImprovementType;
import com.example.demo.model.resources.ResourcesTypes;
import com.example.demo.model.technologies.Technology;

import java.util.ArrayList;

public class InfoController {
    public static String infoResearches() {
        Technology technology =
                GameController.getCivilizations()
                        .get(GameController.getPlayerTurn()).getGettingResearchedTechnology();
        StringBuilder stringBuilder = new StringBuilder();
        if (technology == null) {
            stringBuilder.append("you don't have any technologies in development right now");
            return stringBuilder.toString();
        }
        StringBuilder tempString;
        tempString = new StringBuilder(technology.getTechnologyType() + ": (");
        int cyclesToComplete = TechnologyAndProductionController.cyclesToComplete(technology);
        if (cyclesToComplete == 12345)
            tempString.append("never, your science is 0)");
        else
            tempString.append(cyclesToComplete).append(" cycles to complete)");
        tempString.append("\nwhat unlocks after:");
        for (int i = 0; i < UnitType.VALUES.size(); i++)
            if (UnitType.VALUES.get(i).technologyRequired == technology.getTechnologyType())
                tempString.append(UnitType.VALUES.get(i)).append(" |");
        for (int i = 0; i < ResourcesTypes.VALUES.size(); i++)
            if (ResourcesTypes.VALUES.get(i).technologyTypes == technology.getTechnologyType())
                tempString.append(ResourcesTypes.VALUES.get(i)).append(" |");
        for (int i = 0; i < ImprovementType.VALUES.size(); i++)
            if (ImprovementType.VALUES.get(i).prerequisitesTechnologies == technology.getTechnologyType())
                tempString.append(ImprovementType.VALUES.get(i)).append(" |");
        tempString.append("\n");
        return tempString.toString();
    }

    public static String infoDemographic() {
        Civilization civilization = GameController
                .getCivilizations().get(GameController.getPlayerTurn());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("nickname: ").append(civilization.getUser().getNickname());
        City capital = civilization.getCapital();
        if (capital != null)
            stringBuilder.append(" | capital: ").append(capital.getName());
        stringBuilder.append(" | science: ").append(civilization.collectScience())
                .append(" | happiness: ").append(civilization.getHappiness());
        if (civilization.getHappiness() < 0)
            stringBuilder.append(", unhappy");
        if (civilization.getHappiness() == 0)
            stringBuilder.append(", breathing");
        else
            stringBuilder.append(", happy");
        stringBuilder.append(" | gold: ").append(civilization.getGold()).append(" | units: ")
                .append(civilization.getUnits().size()).append(" | size: ")
                .append(civilization.getSize());
        if (civilization.getResourcesAmount().size() != 0) {
            stringBuilder.append("\nresources: ");
            civilization.getResourcesAmount().forEach((k, v) -> {
                if (v != 0)
                    stringBuilder.append(k).append(": ").append(v).append("\n");
            });
        }
        if (civilization.getUsedLuxuryResources().size() != 0) {
            stringBuilder.append("luxury resources: \n");
            civilization.getUsedLuxuryResources().forEach((k, v) -> {
                if (v)
                    stringBuilder.append(k).append(" |");
            });
        }

        return stringBuilder.toString();
    }

    public static String infoNotifications(int cycles) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = GameController.getCycle(); i > GameController.getCycle() - cycles; i--) {
            if (!GameController.getCivilizations()
                    .get(GameController.getPlayerTurn()).getNotifications().containsKey(i))
                continue;
            ArrayList<String> strings = GameController.getCivilizations()
                    .get(GameController.getPlayerTurn()).getNotifications().get(i);
            for (String string : strings)
                stringBuilder.append(i).append(". ").append(string).append("\n");
        }
        if (stringBuilder.toString().length() == 0)
            stringBuilder.append("you have received no notifications yet");
        return stringBuilder.toString();
    }

    public static String infoEconomic() {
        if (GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities().size() == 0)
            return "you have no cities yet";
        StringBuilder stringBuilder = new StringBuilder();
        for (City city : GameController.getCivilizations()
                .get(GameController.getPlayerTurn()).getCities())
            stringBuilder.append(cityBanner(city));
        return stringBuilder.toString();
    }

    public static String cityBanner(City city) {
        StringBuilder stringBuilder = new StringBuilder();
        if (city == null)
            city = GameController.getSelectedCity();
        if (city == null) {
            stringBuilder.append("no city is selected\n");
            return stringBuilder.toString();
        }
        stringBuilder.append(city.getName()).append(" | founder: ")
                .append(city.getFounder().getUser().getNickname())
                .append(" | mainTileX: ").append(city.getMainTile().getX())
                .append(" | mainTileY: ").append(city.getMainTile().getY())
                .append(" | HP: ").append(city.getHP()).append(" | population: ")
                .append(city.getPopulation()).append(" | food: ")
                .append(city.collectFood()).append(" | citizen: ")
                .append(city.getCitizen()).append(" | founder: ")
                .append(city.getFounder().getUser().getNickname())
                .append(" | defense strength: ")
                .append(city.getCombatStrength(false))
                .append(" | attack strength: ")
                .append(city.getCombatStrength(true))
                .append(" | production: ").append(city.collectProduction())
                .append(" | doesHaveWall: ");
        if (city.findBuilding(BuildingType.WALL) != null)
            stringBuilder.append("Yes");
        else stringBuilder.append("No");
        if (city.getProduct() != null)
            stringBuilder.append(" | product: ")
                    .append(city.getProduct().getName())
                    .append(" - ")
                    .append(city.cyclesToComplete(city.getProduct().getRemainedCost()));
        stringBuilder.append("\nTiles: ");
        for (int i = 0; i < city.getTiles().size(); i++)
            stringBuilder.append(city.getTiles().get(i).getX())
                    .append(", ").append(city.getTiles().get(i).getY())
                    .append(" | ");
        stringBuilder.append("\n");
        for (int i = 0; i < city.getGettingWorkedOnByCitizensTiles().size(); i++)
            stringBuilder.append(city.getGettingWorkedOnByCitizensTiles().get(i).getX())
                    .append(", ")
                    .append(city.getGettingWorkedOnByCitizensTiles().get(i).getY())
                    .append(" | ");
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    static String printUnitInfo(Unit unit) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(unit.getUnitType() +
                ": | Health: " + unit.getHealth() +
                " | currentX: " + unit.getCurrentTile().getX() +
                " | currentY: " + unit.getCurrentTile().getY() +
                " | defense Strength: " + unit.getCombatStrength(false) +
                " | attack Strength: " + unit.getCombatStrength(true) +
                " | movementPoint: " + unit.getMovementPrice() +
                " | state: " + unit.getState());
        if (unit instanceof NonCivilian && (unit.getState() == UnitState.FORTIFY ||
                unit.getState() == UnitState.SETUP))
            stringBuilder.append(" | cycle: ").append(((NonCivilian) unit).getFortifiedCycle());
        if (unit.getDestinationTile() != null)
            stringBuilder.append(" | destinationX: ")
                    .append(unit.getDestinationTile().getX())
                    .append(" destinationY: ").append(unit.getDestinationTile().getY());
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public static String printMilitaryOverview() {
        ArrayList<Unit> units = GameController.getCivilizations()
                .get(GameController.getPlayerTurn()).getUnits();
        StringBuilder stringBuilder = new StringBuilder();
        for (Unit unit : units)
            stringBuilder.append(printUnitInfo(unit));
        if (units.size() == 0)
            stringBuilder.append("you don't have any units right now");
        else
            stringBuilder = new StringBuilder(stringBuilder.substring(0, stringBuilder.length() - 2));
        return stringBuilder.toString();
    }

    //
//    public static String printCities() {
//        StringBuilder stringBuilder = new StringBuilder();
//        ArrayList<City> cities = GameController.getCivilizations()
//                .get(GameController.getPlayerTurn()).getCities();
//
//        for (int i = 0; i < cities.size(); i++)
//            stringBuilder.append(i).append(1).append(". ")
//                    .append(cities.get(i).getName())
//                    .append(" | strength: ")
//                    .append(cities.get(i).getCombatStrength(false))
//                    .append(" | population: ")
//                    .append(cities.get(i).getPopulation()).append("\n");
//        return stringBuilder.toString();
//    }
    public static String printCity(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<City> cities = GameController.getCivilizations()
                .get(GameController.getPlayerTurn()).getCities();

        stringBuilder.append(i+1).append(". ")
                .append(cities.get(i).getName())
                .append(" | strength: ")
                .append(cities.get(i).getCombatStrength(false))
                .append(" | population: ")
                .append(cities.get(i).getPopulation()).append("\n");
        return stringBuilder.toString();
    }

}

