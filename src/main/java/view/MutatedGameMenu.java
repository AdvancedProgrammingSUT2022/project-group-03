package view;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import controller.GameController;
import model.Map;
import model.Units.Unit;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;

public class MutatedGameMenu extends MutatedMenu{
    static class FreeFlagCommands implements Runnable{
        public int run(String name){
            switch (name){
                case "exit":
                    return 2;
                case "menu_show_current":
                    System.out.println("Game Menu");
                    return 3;
                case "next_turn":
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
                case "selected_unit_info"://todo
                    return 3;
                case "city_show_details"://todo
                    return 3;
                case "print_map":
                    System.out.println(GameController.printMap());
                    return 3;
                case "capture_city":
                    cityDestiny(false);
                    return 3;
                case "burn_city":
                    cityDestiny(true);
                    return 3;
                case "build_wall":
                    switch (GameController.buildWall()){
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
                    return 3;
                case "skip_unit_task":
                    switch (GameController.skipUnitTask()){
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
                    return 3;

            }
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
    static class tileXAndYFlag implements Runnable{
        @Parameter(names = { "--tilex", "-x" },
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = true)
        int x;
        @Parameter(names = { "--tiley", "-y" },
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = true)
        int y;
        public int run(String name) {
            switch (name){
                case "map_show":
                    GameController.mapShowPosition(x- Map.WINDOW_X / 2,
                            y - Map.WINDOW_Y / 2 + 1);
                    System.out.println(GameController.printMap());
                    return 3;
                    case "city_attack":
                    switch (GameController.cityAttack(x, y)) {
                        case 0:
                            System.out.println("Attacked successfully");
                            break;
                        case 1:
                            System.out.println("Can not attack there");
                            break;
                        case 2:
                            System.out.println("Out of bond tile");
                            break;
                        case 3:
                            System.out.println("Select your city first");
                            break;
                        case 4:
                            System.out.println("Out of range");
                            break;
                        case 5:
                            System.out.println("Siege need setup before attack");
                            break;

                    }
                    return 3;
            }
            return 3;
        }

    }
    static class tileXAndYFlagSelectUnit implements Runnable {
        @Parameter(names = { "--tilex", "-x" },
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = true)
        int x;
        @Parameter(names = { "--tiley", "-y" },
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = true)
        int y;
        @Parameter(names = { "--civilian", "-c" },
                description = "Id of the Customer who's using the services",
                required = true)
        boolean civilian = false;

        @Parameter(names = { "unit" },
                description = "Id of the Customer who's using the services",
                required = true)
        boolean shit = false;
        public int run(String name) {
            if(!civilian){
                if (GameController.setSelectedCombatUnit(x, y))
                    System.out.println("combat unit selected successfully");
                else
                    System.out.println("selection failed");
            }
            else {
                if (GameController.setSelectedNonCombatUnit(x, y))
                    System.out.println("noncombat unit selected successfully");
                else
                    System.out.println("selection failed");
            }
            return 3;
        }

    }
    static class increase implements Runnable {
        @Parameter(names = { "--amount", "-a" },
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = true)
        int amount;
        @Parameter(names = { "--type", "-t" },
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = true)
        String  type;
        public int run(String name) {
            if(type.equals("gold")|| type.equals("g"))
            {
                GameController.getCivilizations().get(GameController.getPlayerTurn())
                        .increaseGold(amount);
                System.out.println("cheat activated successfully");
            }
            else if(type.equals("turn")|| type.equals("t")){
                for (int i = 0; i < amount * GameController.getCivilizations().size(); i++)
                    GameController.nextTurn();
                System.out.println("cheat activated successfully");
            }else System.out.println("invalid command");
            return 3;
        }


    }

    static class unitState implements Runnable {

        @Parameter (names = { "--state", "-s" },
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        String  state = "init";
        @Parameter (names = { "do", "-d" },
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        String  move = "init";
        @Parameter (names = { "--obj", "-o" },
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        String  object = "init";
        @Parameter(names = { "--tilex", "-x" },
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        int x = -1989;
        @Parameter(names = { "--tiley", "-y" },
                description = "Id of the Customer who's using the services",
                arity = 1,
                required = false)
        int y = -1989;
        public int run(String name) {
            boolean choose = false;
            switch (state){
                case "sleep":
                    switch (GameController.unitSleep()){
                        case 0:
                            System.out.println("the selected unit has been set to sleep successfully");
                            return 3;
                        case 1:
                            System.out.println("no unit is selected");
                            return 3;
                        case 2:
                            System.out.println("the selected unit is not yours");
                            return 3;
                    }
                    return 3;
                case "alert":
                    switch (GameController.unitAlert()){
                        case 0:
                            System.out.println("the selected unit has been set to alert successfully");
                            return 3;
                        case 1:
                            System.out.println("no unit is selected");
                            return 3;
                        case 2:
                            System.out.println("the selected unit is not yours");
                            return 3;
                        case 3:
                            System.out.println("you can't go to alert position when there is enemy around");
                            return 3;
                    }
                    return 3;
                case "fortify":
                    switch (GameController.unitFortify()){
                        case 0:
                            System.out.println("the selected unit has been set to fortify successfully");
                            return 3;
                        case 1:
                            System.out.println("no unit is selected");
                            return 3;
                        case 2:
                            System.out.println("the selected unit is not yours");
                            return 3;
                    }
                    return 3;
                case "fortify_until_heal":
                    switch (GameController.unitFortifyUntilFullHealth()){
                        case 0:
                            System.out.println("the selected unit has been set to fortifyUntilFullHealth successfully");
                            return 3;
                        case 1:
                            System.out.println("no unit is selected");
                            return 3;
                        case 2:
                            System.out.println("the selected unit is not yours");
                            return 3;
                    }
                    return 3;
                case "garrison":
                    switch (GameController.unitGarrison()){
                        case 0:
                            System.out.println("the selected unit has been set to garrison successfully");
                            return 3;
                        case 1:
                            System.out.println("no unit is selected");
                            return 3;
                        case 2:
                            System.out.println("the selected unit is not yours");
                            return 3;
                    }
                    return 3;
                case "wake":
                    switch (GameController.unitWake()) {
                        case 0:
                            System.out.println("the selected unit has been awaken successfully");
                            return 3;
                        case 1:
                            System.out.println("no unit is selected");
                            return 3;
                        case 2:
                            System.out.println("the selected unit is not yours");
                            return 3;
                        case 3:
                            System.out.println("the selected unit is already awake");
                            return 3;
                    }
                    return 3;
                case "init":
                    switch (move){
                        case "init":
                            System.out.println("invalid command");
                            return 3;
                        case "moveto":
                            if(x == -1989 || y == -1989) System.out.println("This command needs x and y");
                            else {
                                if (GameController.unitMoveTo(x, y))
                                    System.out.println("Moved successfully");
                                else
                                    System.out.println("Moving failed");
                                return 3;
                            }
                            return 3;
                        case "attack":
                            if(x == -1989 || y == -1989) System.out.println("This command needs x and y");
                            else {
                                switch (GameController.unitAttack(x, y)) {
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
                                        break;
                                    case 4:
                                        System.out.println("Can't find a route");
                                }
                            }
                            return 3;
                        case "foundcity":
                            switch (GameController.unitWake()) {
                                case 0:
                                    System.out.println("the selected unit has been awaken successfully");
                                    return 3;
                                case 1:
                                    System.out.println("no unit is selected");
                                    return 3;
                                case 2:
                                    System.out.println("the selected unit is not yours");
                                    return 3;
                                case 3:
                                    System.out.println("the selected unit is already awake");
                                    return 3;
                            }
                            return 3;
                        case "pillage":
                            switch (GameController.unitPillage()) {
                                case 4:
                                    System.out.println("Select your unit first");
                                    break;
                                case 3:
                                    System.out.println("Nothing to pillage");
                                    break;
                                case 0:
                                    System.out.println("Pillaged successfully");
                            }
                            return 3;
                        case "skip":
                            switch (GameController.skipUnitTask()) {
                                case 0:
                                    System.out.println("task skipped successfully");
                                    return 3;
                                case 1:
                                    System.out.println("no unit is selected");
                                    return 3;
                                case 2:
                                    System.out.println("the selected unit is not yours");
                                    return 3;
                                case 3:
                                    System.out.println("the selected unit does not need a task");
                                    return 3;
                            }
                            return 3;
                        case "delete":
                            switch (GameController.unitDelete(GameController.getSelectedUnit())) {
                                case 0:
                                    System.out.println("the selected unit has been deleted successfully");
                                    return 3;
                                case 1:
                                    System.out.println("no unit is selected");
                                    return 3;
                                case 2:
                                    System.out.println("the selected unit is not yours");
                                    return 3;
                            }
                            return 3;
                        case "cancel_mission":
                            switch (GameController.unitCancelMission()) {
                                case 0:
                                    System.out.println("mission cancelled successfully");
                                    return 3;
                                case 1:
                                    System.out.println("not unit is selected");
                                    return 3;
                                case 2:
                                    System.out.println("the selected unit is not yours");
                                    return 3;
                                case 3:
                                    System.out.println("the selected unit has no missions");
                                    return 3;
                            }
                            return 3;
                        case "remove":
                            if(object.equals("jungle")) choose = true;
                            else if(object.equals("route")) choose = false;
                            else return 3;
                            switch (GameController.unitRemoveFromTile(choose)){
                                case 0:
                                    System.out.println(object + "'s removal from the tile operation started successfully");
                                    return 3;
                                case 1:
                                    System.out.println("no unit is selected");
                                    return 3;
                                case 2:
                                    System.out.println("the selected unit is not yours");
                                    return 3;
                                case 3:
                                    System.out.println("the selected unit is not a worker");
                                    return 3;
                                case 4:
                                    System.out.println("the selected tile does not have a jungle");
                                    return 3;
                                case 5:
                                    System.out.println("the selected tile does not have any road or railroad");
                                    return 3;
                            }
                            return 3;
                        case "repair":
                            switch (GameController.unitRepair()){
                                case 0:
                                    System.out.println("improvement repaired successfully");
                                    return 3;
                                case 1:
                                    System.out.println("no unit is selected");
                                    return 3;
                                case 2:
                                    System.out.println("the selected unit is not yours");
                                    return 3;
                                case 3:
                                    System.out.println("the selected unit is not a worker");
                                    return 3;
                                case 4:
                                    System.out.println("there are no improvements here");
                                    return 3;
                                case 5:
                                    System.out.println("the selected improvement does not need repairing");
                                    return 3;
                            }
                            return 3;

                    }


            }
            return 3;
        }


    }



        protected JCommander jCommander(){
        JCommander jCommander = new JCommander();
        jCommander.setAllowAbbreviatedOptions(true);
        jCommander.addCommand("unit",new increase());
        jCommander.addCommand("increase",new increase());
        jCommander.addCommand("select",new tileXAndYFlagSelectUnit());
        jCommander.addCommand("city_attack",new tileXAndYFlag());
        jCommander.addCommand("map_show",new tileXAndYFlag());
        jCommander.addCommand("select_city",new tileXAndYFlag());
        jCommander.addCommand("exit",new FreeFlagCommands());
        jCommander.addCommand("menu_show_current",new FreeFlagCommands());
        jCommander.addCommand("next_turn",new FreeFlagCommands());
        jCommander.addCommand("selected_unit_info",new FreeFlagCommands());
        jCommander.addCommand("city_show_details",new FreeFlagCommands());
        jCommander.addCommand("print_map",new FreeFlagCommands());
        jCommander.addCommand("capture_city",new FreeFlagCommands());
        jCommander.addCommand("burn_city",new FreeFlagCommands());
        jCommander.addCommand("build_wall",new FreeFlagCommands());
        jCommander.addCommand("skip_unit_task",new FreeFlagCommands());
        jCommander.addCommand("burn_city",new FreeFlagCommands());
        jCommander.addCommand("burn_city",new FreeFlagCommands());
        return jCommander;
    }

    public static void main(String[] args) {
        new MutatedGameMenu().run(new Scanner(System.in),3);
    }



}
