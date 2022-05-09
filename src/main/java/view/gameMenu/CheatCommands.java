package view.gameMenu;

import com.beust.jcommander.Parameter;
import controller.gameController.GameController;
import controller.gameController.InfoCommandsController;
import model.Units.UnitType;
import model.resources.ResourcesTypes;
import model.technologies.TechnologyType;
import view.Runnable;

public class CheatCommands {
    static class InnerClass implements Runnable {
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
                InfoCommandsController.openMap();
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
    private static void cheatCaptureCity(String name) {
        switch (InfoCommandsController.cheatCaptureCity(name)) {
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
        switch (InfoCommandsController.cheatTechnology(technologyType)) {
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
        switch (InfoCommandsController.cheatProduction(amount)) {
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
        switch (InfoCommandsController.cheatMoveIt(x, y)) {
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
        InfoCommandsController.cheatResource(amount, resourcesTypes);
        System.out.println("cheat activated successfully");
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
        if (InfoCommandsController.cheatRoadEverywhere() == 0)
            System.out.println("cheat activated successfully");
    }

}
