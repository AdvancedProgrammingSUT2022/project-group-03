package view.gameMenu;

import controller.gameController.GameController;
import view.Runnable;

public class FreeFlagCommands {
    static class InnerClass implements Runnable {
        public int run(String name) {
            if ("next-turn".equals(name)) {
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
            } else {
                System.out.println("invalid command");
            }
            return 3;
        }
    }
}
