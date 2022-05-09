package view.gameMenu;

import com.beust.jcommander.Parameter;
import controller.gameController.GameController;
import view.Runnable;

public class IncreaseView {
    static class InnerClass implements Runnable {
        @Parameter(names = {"--turn", "-t"},
                description = "the command that chooses changing turns",
                arity = 1)
        int turn = -1989;
        @Parameter(names = {"--gold", "-g"},
                description = "the command that chooses increasing gold",
                arity = 1)
        int gold = -1989;

        public int run(String name) {
            if (gold != -1989) {
                GameController.getCivilizations()
                        .get(GameController.getPlayerTurn()).increaseGold(gold);
                System.out.println("cheat activated successfully");
            } else if (turn != 1989) {
                for (int i = 0; i < turn * GameController.getCivilizations().size(); i++)
                    GameController.nextTurn();
                System.out.println("cheat activated successfully");
            } else System.out.println("invalid command");
            return 3;
        }
    }
}
