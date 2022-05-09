package view.gameMenu;

import com.beust.jcommander.Parameter;
import controller.gameController.GameController;
import model.City;
import model.resources.ResourcesTypes;
import view.Runnable;

import java.util.HashMap;

public class CityCommands {
    static class InnerClass implements Runnable {
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
    }
    private static void cityDestiny(boolean burn) {
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
}
