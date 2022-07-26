package model;

import controller.gameController.GameController;

public interface CanGetAttacked {
    boolean checkToDestroy(GameController gameController);

    double getCombatStrength(boolean isAttack);

    void takeDamage(int amount,Civilization civilization,GameController gameController);
}
