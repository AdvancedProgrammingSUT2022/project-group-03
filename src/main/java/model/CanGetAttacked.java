package model;

public interface CanGetAttacked {
    public boolean checkToDestroy();

    public double getCombatStrength(boolean isAttack);

    public void takeDamage(int amount);
}
