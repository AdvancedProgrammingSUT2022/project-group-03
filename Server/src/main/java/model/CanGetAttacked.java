package model;

import controller.gameController.GameController;
import network.MySocketHandler;

public interface CanGetAttacked {
    boolean checkToDestroy(GameController gameController, MySocketHandler socketHandler);

    double getCombatStrength(boolean isAttack);

    void takeDamage(int amount,Civilization civilization,GameController gameController,MySocketHandler socketHandler);
}
