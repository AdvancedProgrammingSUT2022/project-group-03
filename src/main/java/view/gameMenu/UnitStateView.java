package view.gameMenu;

import com.beust.jcommander.Parameter;
import controller.gameController.GameController;
import controller.gameController.UnitStateController;
import model.improvements.ImprovementType;
import view.Runnable;

public class UnitStateView {
    static class InnerClass implements Runnable {
        @Parameter(names = {"--state", "-s"},
                description = "state of the selected unit",
                arity = 1)
        String state = "init";
        @Parameter(names = {"do", "-d"},
                description = "doing an operation like building, repairing and...",
                arity = 1)
        String move = "init";
        @Parameter(names = {"--object", "-o"},
                description = "the entry object",
                arity = 1)
        String object = "init";
        @Parameter(names = {"--name", "-n"},
                description = "the name of the entry",
                arity = 1)
        String nameCity = "init";
        @Parameter(names = {"--tilex", "-x"},
                description = "X of the entry tile",
                arity = 1)
        int x = -1989;
        @Parameter(names = {"--tiley", "-y"},
                description = "Y of the entry tile",
                arity = 1)
        int y = -1989;

        public int run(String name) {
            switch (state) {
                case "sleep":
                    unitSleep();
                    return 3;
                case "alert":
                    unitAlert();
                    return 3;
                case "fortify":
                    unitFortify(false);
                    return 3;
                case "fortify_until_heal":
                    unitFortify(true);
                    return 3;
                case "garrison":
                    unitGarrison();
                    return 3;
                case "wake":
                    unitWake();
                    return 3;
                case "setup":
                    unitSetupRanged();
                    return 3;
                case "init":
                    switch (move) {
                        case "moveto":
                            unitMoveTo(x, y);
                            return 3;
                        case "attack":
                            unitAttack(x, y);
                            return 3;
                        case "found_city":
                            unitFoundCity(nameCity);
                            return 3;
                        case "pillage":
                            unitPillage();
                            return 3;
                        case "skip":
                            skipUnitTask();
                            return 3;
                        case "delete":
                            unitDelete();
                            return 3;
                        case "cancel_mission":
                            unitCancelMission();
                            return 3;
                        case "remove":
                            if (!object.equals("init"))
                                unitRemoveFromTile(object);
                            return 3;
                        case "repair":
                            unitRepair();
                            return 3;
                        case "build":
                            if (object.equals("init"))
                                System.out.println("invalid command");
                            else
                                unitBuild(object);
                            return 3;
                        default:
                            System.out.println("invalid command");
                            return 3;
                    }
                default:
                    System.out.println("invalid command");
            }
            return 3;
        }
    }

    private static void unitSleep() {
        switch (UnitStateController.unitSleep()) {
            case 0:
                System.out.println("the selected unit has been set to sleep successfully");
                break;
            case 1:
                System.out.println("no unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
        }
    }

    private static void unitAlert() {
        switch (UnitStateController.unitAlert()) {
            case 0:
                System.out.println("the selected unit has been set to alert successfully");
                break;
            case 1:
                System.out.println("no unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
            case 3:
                System.out.println("you can't go to alert position when there is enemy around");
                break;
        }
    }

    private static void unitFortify(boolean untilFullHealth) {
        int boolToInt = 0;
        if (untilFullHealth)
            boolToInt = 1;
        switch (UnitStateController.unitChangeState(boolToInt)) {
            case 0:
                if (untilFullHealth)
                    System.out.println("the selected unit has been set to fortifyUntilFullHealth successfully");
                else
                    System.out.println("the selected unit has been set to fortify successfully");
                break;
            case 1:
                System.out.println("no unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
            case 3:
                System.out.println("the selected unit is a civilian");
                break;
        }
    }

    private static void unitGarrison() {
        switch (UnitStateController.unitChangeState(2)) {
            case 0:
                System.out.println("the selected unit has been set to garrison successfully");
                break;
            case 1:
                System.out.println("no unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
            case 3:
                System.out.println("the selected unit is a civilian");
                break;
        }
    }

    private static void unitWake() {
        switch (UnitStateController.unitChangeState(3)) {
            case 0:
                System.out.println("the selected unit has been awaken successfully");
                break;
            case 1:
                System.out.println("no unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
            case 3:
                System.out.println("the selected unit is already awake");
                break;
        }
    }

    private static void unitSetupRanged() {
        switch (UnitStateController.unitSetupRanged()) {
            case 0:
                System.out.println("the selected unit started setup successfully");
                break;
            case 1:
                System.out.println("no unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
            case 3:
                System.out.println("you can't setup this unit");
                break;
        }
    }

    private static void unitMoveTo(int x, int y) {
        if (x == -1989 || y == -1989)
            System.out.println("This command needs x and y");
        else {
            if (UnitStateController.unitMoveTo(x, y))
                System.out.println("Moved successfully");
            else
                System.out.println("Moving failed");
        }
    }

    private static void unitAttack(int x, int y) {
        if (x == -1989 || y == -1989) System.out.println("This command needs x and y");
        else
            switch (UnitStateController.unitAttack(x, y)) {
                case 0:
                    System.out.println("Attacked successfully");
                    break;
                case 1:
                    System.out.println("no unit is selected");
                    break;
                case 2:
                    System.out.println("the selected unit is not yours");
                    break;
                case 3:
                    System.out.println("the selected unit is not a nonCivilian");
                    break;
                case 4:
                    System.out.println("the entered tile is not valid");
                    break;
                case 5:
                    System.out.println("there is nothing to attack to");
                    break;
                case 6:
                    System.out.println("can't attack");
                    break;
                case 7:
                    System.out.println("need setup");
                    break;
                case 8:
                    System.out.println("already attacked");
                    break;
            }
    }

    private static void unitFoundCity(String name) {
        if (name.equals("init")) {
            System.out.println("please insert a name");
            return;
        }
        switch (UnitStateController.unitFoundCity(name)) {
            case 0:
                System.out.println("city founded successfully");
                break;
            case 1:
                System.out.println("not unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
            case 3:
                System.out.println("the selected unit is not a settler");
                break;
            case 4:
                System.out.println("the selected tile is occupied with another city");
                break;
            case 5:
                System.out.println("the entered name is used already");
                break;
        }
    }

    private static void unitPillage() {
        switch (UnitStateController.unitPillage()) {
            case 4:
                System.out.println("Select your unit first");
                break;
            case 3:
                System.out.println("Nothing to pillage");
                break;
            case 0:
                System.out.println("Pillaged successfully");
                break;
        }
    }

    private static void skipUnitTask() {
        switch (UnitStateController.skipUnitTask()) {
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
    }

    private static void unitDelete() {
        switch (UnitStateController.unitDelete(GameController.getSelectedUnit())) {
            case 0:
                System.out.println("the selected unit has been deleted successfully");
                break;
            case 1:
                System.out.println("no unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
        }
    }

    private static void unitCancelMission() {
        switch (UnitStateController.unitCancelMission()) {
            case 0:
                System.out.println("mission cancelled successfully");
                break;
            case 1:
                System.out.println("not unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
            case 3:
                System.out.println("the selected unit has no missions");
                break;
        }
    }

    private static void unitRemoveFromTile(String object) {
        boolean choose;
        if (object.equals("jungle")) choose = true;
        else if (object.equals("route")) choose = false;
        else return;
        switch (UnitStateController.unitRemoveFromTile(choose)) {
            case 0:
                System.out.println(object + "'s removal from the tile operation started successfully");
                break;
            case 1:
                System.out.println("no unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
            case 3:
                System.out.println("the selected unit is not a worker");
                break;
            case 4:
                System.out.println("the selected tile does not have a jungle");
                break;
            case 5:
                System.out.println("the selected tile does not have any road or railroad");
                break;
        }
    }

    private static void unitRepair() {
        switch (UnitStateController.unitRepair()) {
            case 0:
                System.out.println("improvement repaired successfully");
                break;
            case 1:
                System.out.println("no unit is selected");
                break;
            case 2:
                System.out.println("the selected unit is not yours");
                break;
            case 3:
                System.out.println("the selected unit is not a worker");
                break;
            case 4:
                System.out.println("there are no improvements here");
                break;
            case 5:
                System.out.println("the selected improvement does not need repairing");
                break;
        }
    }

    private static void unitBuild(String object) {
        ImprovementType improvementType = ImprovementType.stringToImprovementType(object);
        int result;
        if (improvementType != null)
            result = UnitStateController.unitBuild(improvementType);
        else if (object.equals("road"))
            result = UnitStateController.unitBuildRoad();
        else if (object.equals("railroad"))
            result = UnitStateController.unitBuildRailRoad();
        else {
            System.out.println("no improvement with that name exists");
            return;
        }
        if (result == 0)
            System.out.println(object + "'s production started successfully");
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
        if (result == 6)
            System.out.println("you already have one here");
    }
}
