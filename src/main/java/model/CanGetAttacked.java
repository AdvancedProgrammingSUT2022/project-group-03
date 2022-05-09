package model;

public interface CanGetAttacked {
    boolean checkToDestroy();

    double getCombatStrength(boolean isAttack);

    void takeDamage(int amount);
}
