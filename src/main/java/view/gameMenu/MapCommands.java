package view.gameMenu;

import com.beust.jcommander.Parameter;
import controller.gameController.GameController;
import model.Map;
import view.Runnable;

public class MapCommands {
    static class InnerClass implements Runnable {
        @Parameter(names = {"print", "-p"},
                description = "prints out tha map")
        boolean print = false;
        @Parameter(names = {"show", "-s"},
                description = "shows the thing you choose")
        boolean show = false;
        @Parameter(names = {"--city", "-c"},
                description = "shows the place of the city on the map")
        String city = "init";
        @Parameter(names = {"--tilex", "-x"},
                description = "X of the entry tile")
        int x = -1989;
        @Parameter(names = {"--tiley", "-y"},
                description = "Y of the entry tile",
                arity = 1)
        int y = -1989;
        @Parameter(names = {"--direction", "-d"},
                description = "direction to move the screen",
                arity = 1)
        String dir = "init";
        @Parameter(names = {"move", "-m"},
                description = "this command is used to move the screen")
        boolean move = false;
        @Parameter(names = {"--amount", "-a"},
                description = "this command is used to get the needed amount",
                arity = 1)
        int amount = -1989;

        public int run(String name) {
            if (print && !move && !show && city.equals("init"))
                System.out.println(GameController.printMap());
            else if (show && !city.equals("init") && (x == -1989 && y == -1989) && !move)
                mapShowCityName(city);
            else if (show && !move && (x != -1989 && y != -1989)) {
                GameController.mapShowPosition(x - Map.WINDOW_X / 2,
                        y - Map.WINDOW_Y / 2 + 1);
                System.out.println(GameController.printMap());
            } else if (move) {
                if (dir.matches("[RULD]")) {
                    if (amount == -1989)
                        GameController.mapMove(1, dir);
                    else GameController.mapMove(amount, dir);
                    System.out.println("map moved successfully");
                }
            } else System.out.println("invalid command");
            return 3;
        }
    }

    private static void mapShowCityName(String city) {
        switch (GameController.mapShowCityName(city)) {
            case 0:
                System.out.println("city selected successfully");
                break;
            case 1:
                System.out.println("no city with this name exists");
                break;
            case 2:
                System.out.println("you have not unlocked the position of this city yet");
                break;
        }
    }
}
