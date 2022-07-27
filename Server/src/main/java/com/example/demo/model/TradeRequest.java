package com.example.demo.model;

import com.example.demo.model.resources.ResourcesTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public record TradeRequest(
        ArrayList<Map.Entry<ResourcesTypes, Integer>> theirOffers,
        ArrayList<Map.Entry<ResourcesTypes, Integer>> yourOffers,
        Civilization from, Civilization to, int theirGoldOffer,
        int yourGoldOffer) implements Serializable {

    public ArrayList<Map.Entry<ResourcesTypes, Integer>> getTheirOffers() {
        return theirOffers;
    }

    public ArrayList<Map.Entry<ResourcesTypes, Integer>> getYourOffers() {
        return yourOffers;
    }

    public Civilization getTo() {
        return to;
    }

    public Civilization getFrom() {
        return from;
    }

}
