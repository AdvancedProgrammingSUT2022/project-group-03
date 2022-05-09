package view.gameMenu;

import com.beust.jcommander.Parameter;
import controller.gameController.GameController;
import view.Runnable;

public class TileXAndYFlagSelectUnit {
    static class InnerClass implements Runnable {
        @Parameter(names = {"--tilex", "-x"},
                description = "X of the entry tile",
                arity = 1)
        int x = -1989;
        @Parameter(names = {"--tiley", "-y"},
                description = "Y of the entry tile",
                arity = 1)
        int y = -1989;
        @Parameter(names = {"--object", "-o"},
                description = "the entry object",
                arity = 1,
                required = true)
        String type;
        @Parameter(names = {"--name", "-n"},
                description = "the name of the entry",
                arity = 1)
        String name = "init";

        public int run(String command) {
            if ((type.equals("noncivilian") || type.equals("ncu")) && x != -1989 && y != -1989) {
                if (GameController.setSelectedNonCivilian(x, y))
                    System.out.println("noncivilian unit selected successfully");
                else
                    System.out.println("selection failed");
            } else if ((type.equals("civilian") || type.equals("cu")) && x != -1989 && y != -1989) {
                if (GameController.setSelectedCivilian(x, y))
                    System.out.println("civilian unit selected successfully");
                else
                    System.out.println("selection failed");
            } else if ((type.equals("city") || type.equals("c")) && x != -1989 && y != -1989) {
                if (!GameController.setSelectedCityByPosition(x, y))
                    System.out.println("city does not exist");
                else
                    System.out.println("city selected successfully");
            } else if ((type.equals("city") || type.equals("c")) && !name.equals("init")) {
                if (!GameController.setSelectedCityByName(name))
                    System.out.println("city does not exist");
                else
                    System.out.println("city selected successfully");
            } else System.out.println("invalid command");
            return 3;
        }

    }
}
