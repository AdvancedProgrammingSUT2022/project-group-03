package model;

public interface CanGetAttacked {
    public boolean checkToDestroy();

    public int getCombatStrength(boolean isAttack);

    public void takeDamage(int amount);
}
