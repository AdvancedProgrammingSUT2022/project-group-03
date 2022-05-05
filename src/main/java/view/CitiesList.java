package view;

import controller.GameController;
import model.City;

import java.util.ArrayList;

public class CitiesList extends Menu {
    {
        regexes = new String[]{
                "^menu exit$",
                "^menu show-current$",
                "^(\\d+)$",
                "^economic overview$"
        };
    }

    public void printCities()
    {
        ArrayList <City> cities = GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities();
        for (int i = 0; i < cities.size(); i++)
            System.out.println(i+1 + ". " + cities.get(i).getName()
                    + " | strength: "+ cities.get(i).getCombatStrength(false)
                    + " | population: " + cities.get(i).getPopulation());
    }

    public void openCityBanner(String command)
    {
        ArrayList <City> cities = GameController.getCivilizations().get(GameController.getPlayerTurn()).getCities();
        int number = Integer.parseInt(command);
        if(number<1 || number>cities.size())
        {
            System.out.println("invalid number");
            return;
        }
        cityBanner(cities.get(number-1));
    }
    public static void cityBanner(City city) {
        if(city==null)
            city=GameController.getSelectedCity();
        if(city==null)
        {
            System.out.println("no city is selected");
            return;
        }
        System.out.print(city.getName() +
                " | mainTileX: " + city.getMainTile().getX() +
                " | mainTileY: " + city.getMainTile().getY() +
                " | population: " + city.getPopulation() +
                " | food: " + city.collectFood() +
                " | citizen: " + city.getCitizen() +
                " | founder: " + city.getFounder().getUser().getNickname() +
                " | defense strength: " + city.getCombatStrength(false) +
                " | attack strength: " + city.getCombatStrength(true) +
                " | production: " + city.collectProduction() +
                " | doesHaveWall: ");
        if (city.getWall() != null)
            System.out.print("Yes");
        else System.out.print("No");
        if(city.getProduct()!=null)
            System.out.print(" | product: " + city.getProduct().getName()
                    + " - " + city.cyclesToComplete(city.getProduct().getRemainedCost()));
        System.out.println("\nTiles: ");
        for (int i = 0; i < city.getTiles().size(); i++)
            System.out.print(city.getTiles().get(i).getX() + ", " + city.getTiles().get(i).getY() + " |");
        System.out.println();
        for (int i = 0; i < city.getGettingWorkedOnByCitizensTiles().size(); i++)
            System.out.print(city.getGettingWorkedOnByCitizensTiles().get(i).getX() +
                    ", " + city.getGettingWorkedOnByCitizensTiles().get(i).getY() + " |");
        System.out.println();
    }

    @Override
    protected boolean commands(String command) {
        commandNumber = getCommandNumber(command, regexes,true);
        switch (commandNumber) {
            case -1:
                System.out.println("invalid command");
                break;
            case 0:
                return true;
            case 1:
                System.out.println("Login Menu");
                break;
            case 2:
                openCityBanner(command);
                break;
            case 3:
                GameMenu.infoEconomic();
                break;
        }
        return false;
    }
}
