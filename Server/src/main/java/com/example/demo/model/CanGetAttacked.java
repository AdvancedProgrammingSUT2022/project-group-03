package com.example.demo.model;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.network.MySocketHandler;

public interface CanGetAttacked {
    boolean checkToDestroy(GameController gameController, MySocketHandler socketHandler);

    double getCombatStrength(boolean isAttack);

    void takeDamage(int amount,Civilization civilization,GameController gameController,MySocketHandler socketHandler);
}
