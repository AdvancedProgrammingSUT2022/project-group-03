package model;

public interface Producible {
    int getCost();

    int getRemainedCost();

    void setRemainedCost(int remainedCost);

    String getName();
}
