package com.example.demo.view;

import com.example.demo.model.Civilization;
import com.example.demo.model.resources.ResourcesTypes;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;

public record TradeRequest(
        ArrayList<Pair<ResourcesTypes, Integer>> theirOffers,
        ArrayList<Pair<ResourcesTypes, Integer>> yourOffers,
        Civilization from, Civilization to, int theirGoldOffer,
        int yourGoldOffer) implements Serializable {

    public ArrayList<Pair<ResourcesTypes, Integer>> getTheirOffers() {
        return theirOffers;
    }

    public ArrayList<Pair<ResourcesTypes, Integer>> getYourOffers() {
        return yourOffers;
    }

    public Civilization getTo() {
        return to;
    }

    public Civilization getFrom() {
        return from;
    }

}
