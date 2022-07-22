package com.example.demo.view;

import com.example.demo.model.Civilization;
import com.example.demo.model.resources.ResourcesTypes;
import javafx.util.Pair;

import java.util.ArrayList;

public class TradeRequest {
    private final ArrayList<Pair<ResourcesTypes, Integer>> theirOffers;
    private final ArrayList<Pair<ResourcesTypes, Integer>> yourOffers;
    private final Civilization from;
    private final Civilization to;

    public TradeRequest(ArrayList<Pair<ResourcesTypes, Integer>> theirOffers,
                        ArrayList<Pair<ResourcesTypes, Integer>> yourOffers,
                        Civilization from, Civilization to) {
        this.theirOffers = theirOffers;
        this.yourOffers = yourOffers;
        this.from = from;
        this.to = to;
    }

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
