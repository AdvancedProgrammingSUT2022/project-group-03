package com.example.demo.model.building;

import com.example.demo.model.technologies.TechnologyType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public enum BuildingType {
    BARRACKS(80, 1, TechnologyType.BRONZE_WORKING),
    GRANARY(100, 1, TechnologyType.POTTERY),
    LIBRARY(80, 1, TechnologyType.WRITING),
    MONUMENT(60, 1, null),
    WALLS(100, 0, TechnologyType.MASONRY),
    WATER_MILL(120,2,TechnologyType.THE_WHEEL),
    ARMORY(130,3,TechnologyType.IRON_WORKING),
    BURIAL_TOMB(120,0,TechnologyType.PHILOSOPHY),
    CIRCUS(150,3,TechnologyType.HORSEBACK_RIDING),
    COLOSSEUM(150,3,TechnologyType.CONSTRUCTION),
    COURTHOUSE(200,5,TechnologyType.MATHEMATICS),
    STABLE(100,1,TechnologyType.HORSEBACK_RIDING),
    TEMPLE(120,2,TechnologyType.PHILOSOPHY),
    CASTLE(200,3,TechnologyType.CHIVALRY),
    FORGE(150,2,TechnologyType.METAL_CASTING),
    GARDEN(120,2,TechnologyType.THEOLOGY),
    MARKET(120,0,TechnologyType.CURRENCY),
    MINT(120,0,TechnologyType.CURRENCY),
    MONASTERY(120,2,TechnologyType.THEOLOGY),
    UNIVERSITY(200,3,TechnologyType.EDUCATION),
    WORKSHOP(100,2,TechnologyType.METAL_CASTING),
    BANK(220,0,TechnologyType.BANKING),
    MILITARY_ACADEMY(350,3,TechnologyType.MILITARY_SCIENCE),
    MUSEUM(350,3,TechnologyType.ARCHAEOLOGY),
    OPERA_HOUSE(220,3,TechnologyType.ACOUSTICS),
    PUBLIC_SCHOOL(350,3,TechnologyType.SCIENTIFIC_THEORY),
    SATRAPS_COURT(220,0,TechnologyType.BANKING),
    THEATER(300,5,TechnologyType.PRINTING_PRESS),
    WINDMILL(180,2,TechnologyType.ECONOMICS),
    ARSENAL(350,3,TechnologyType.RAILROAD),
    BROADCAST_TOWER(600,3,TechnologyType.RADIO),
    FACTORY(300,3,TechnologyType.STEAM_POWER),
    HOSPITAL(400,2,TechnologyType.BIOLOGY),
    MILITARY_BASE(450,4,TechnologyType.TELEGRAPH),
    STOCK_EXCHANGE(650,0,TechnologyType.ELECTRICITY);

    private final int cost;
    public final int maintenance;
    public final TechnologyType technologyType;
    public static final HashMap<BuildingType, ArrayList<BuildingType>> prerequisites = new HashMap<>();
    static {
        prerequisites.put(BARRACKS, new ArrayList<>());
        prerequisites.put(GRANARY, new ArrayList<>());
        prerequisites.put(LIBRARY, new ArrayList<>());
        prerequisites.put(MONUMENT, new ArrayList<>());
        prerequisites.put(WALLS, new ArrayList<>());
        prerequisites.put(WATER_MILL, new ArrayList<>());
        prerequisites.put(ARMORY, new ArrayList<>(List.of(BARRACKS)));
        prerequisites.put(BURIAL_TOMB, new ArrayList<>());
        prerequisites.put(CIRCUS, new ArrayList<>());
        prerequisites.put(COLOSSEUM, new ArrayList<>());
        prerequisites.put(COURTHOUSE, new ArrayList<>());
        prerequisites.put(STABLE, new ArrayList<>());
        prerequisites.put(TEMPLE, new ArrayList<>(List.of(MONUMENT)));
        prerequisites.put(CASTLE, new ArrayList<>(List.of(WALLS)));
        prerequisites.put(FORGE, new ArrayList<>());
        prerequisites.put(GARDEN, new ArrayList<>());
        prerequisites.put(MARKET, new ArrayList<>());
        prerequisites.put(MINT, new ArrayList<>());
        prerequisites.put(MONASTERY, new ArrayList<>());
        prerequisites.put(UNIVERSITY, new ArrayList<>(List.of(LIBRARY)));
        prerequisites.put(WORKSHOP, new ArrayList<>());
        prerequisites.put(BANK, new ArrayList<>(List.of(MARKET)));
        prerequisites.put(MILITARY_ACADEMY, new ArrayList<>(List.of(BARRACKS)));
        prerequisites.put(MUSEUM, new ArrayList<>(List.of(OPERA_HOUSE)));
        prerequisites.put(OPERA_HOUSE, new ArrayList<>(List.of(TEMPLE,BURIAL_TOMB)));
        prerequisites.put(PUBLIC_SCHOOL, new ArrayList<>(List.of(UNIVERSITY)));
        prerequisites.put(SATRAPS_COURT, new ArrayList<>(List.of(MARKET)));
        prerequisites.put(THEATER, new ArrayList<>(List.of(COLOSSEUM)));
        prerequisites.put(WINDMILL, new ArrayList<>());
        prerequisites.put(ARSENAL, new ArrayList<>(List.of(MILITARY_ACADEMY)));
        prerequisites.put(BROADCAST_TOWER, new ArrayList<>(List.of(MUSEUM)));
        prerequisites.put(FACTORY, new ArrayList<>());
        prerequisites.put(HOSPITAL, new ArrayList<>());
        prerequisites.put(MILITARY_BASE, new ArrayList<>(List.of(CASTLE)));
        prerequisites.put(STOCK_EXCHANGE, new ArrayList<>(List.of(BANK,SATRAPS_COURT)));


    }


    BuildingType(int cost, int maintenance, TechnologyType technologyType) {
        this.cost = cost;
        this.maintenance = maintenance;
        this.technologyType = technologyType;
    }

    public static final List<BuildingType> VALUES = List.of(values());

    public int getCost() {
        return cost;
    }
}
