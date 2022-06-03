package com.example.demo.model;

public interface CanGetAttacked {
    boolean checkToDestroy();

    double getCombatStrength(boolean isAttack);

    void takeDamage(int amount,Civilization civilization);
}
