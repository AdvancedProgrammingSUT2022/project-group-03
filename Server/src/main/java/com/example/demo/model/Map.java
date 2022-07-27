package com.example.demo.model;

import com.example.demo.model.Units.Civilian;
import com.example.demo.model.Units.Unit;

import com.example.demo.model.Units.UnitType;
import com.example.demo.model.features.Feature;
import com.example.demo.model.features.FeatureType;
import com.example.demo.model.improvements.ImprovementType;
import com.example.demo.model.resources.ResourcesTypes;
import com.example.demo.model.tiles.Tile;
import com.example.demo.model.tiles.TileType;
import com.example.demo.network.MySocketHandler;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Map implements Serializable {
    @Serial
    private static final long serialVersionUID = 5934131344211415395L;
    public final  int WINDOW_X = 5;
    public final  int WINDOW_Y = 14;
    private Tile[][] tiles;
    private  int x;
    private  int y;
    private final Random random = new Random();


    public Tile[][] getTiles() {
        return tiles;
    }

    public Map(ArrayList<Civilization> civilizations,int x,int y) {
        this.x = x;
        this.y = y;
        GenerateMap(civilizations);
    }

    public void addStartingSettlers(ArrayList<Civilization> civilizations, MySocketHandler socketHandler) {
        int[][] settlers = new int[2][civilizations.size()];
        for (int i = 0; i < civilizations.size(); i++) {
            boolean end = false;
            int settlerX = 0;
            int settlerY = 0;
            while (!end) {
                end = true;
                settlerX = 3 + random.nextInt(x - 6);
                settlerY = 5 + random.nextInt(x - 10);
                if (coordinatesToTile(settlerX, settlerY).getMovingPrice() > 123 || coordinatesToTile(settlerX, settlerY).getRuins() != null) {
                    end = false;
                    continue;
                }
                for (int j = 0; j < i; j++)
                    if (Math.abs(settlerX - settlers[0][j]) < 5 &&
                            Math.abs(settlers[1][j] - settlerX) < 5) {
                        end = false;
                        break;
                    }
            }
            Civilian hardcodeUnit = new Civilian(coordinatesToTile(settlerX, settlerY),
                    civilizations.get(i), UnitType.SETTLER);
            hardcodeUnit.setRemainedCost(0);
            civilizations.get(i).getUnits().add(hardcodeUnit);
            coordinatesToTile(settlerX, settlerY).setCivilian(hardcodeUnit,socketHandler);
            if (i == 0)
                socketHandler.getGame().getGameController().openNewArea(coordinatesToTile(settlerX, settlerY),
                        civilizations.get(i), hardcodeUnit);
            settlers[0][i] = settlerX;
            settlers[1][i] = settlerY;
////            City city = new City(GameController.getMap().tiles[settlerX][settlerY], "wtf", civilizations.get(i));
////            GameController.getMap().tiles[settlerX][settlerY].setCity(city);
////            civilizations.get(i).getCities().add(city);
////            civilizations.get(GameController.getPlayerTurn()).getCities().add(city);

////            Civilian civilian = new Civilian(coordinatesToTile(settlerX+2, settlerY),civilizations.get(i),UnitType.WORKER);
////            civilizations.get(i).getUnits().add(civilian);
////            coordinatesToTile(settlerX+2,settlerY).setCivilian(civilian);
////            civilian.setRemainedCost(0);



//
//            if (coordinatesToTile(settlerX + 1, settlerY).getRuins() == null) {
//                Civilian civilian = new Civilian(coordinatesToTile(settlerX + 1, settlerY), civilizations.get(i), UnitType.WORKER);
//                civilizations.get(i).getUnits().add(civilian);
//                coordinatesToTile(settlerX + 1, settlerY).setCivilian(civilian);
//                civilian.setRemainedCost(0);
//            }
//            if (coordinatesToTile(settlerX + 1, settlerY + 1).getRuins() == null) {
//
//                Civilian civilian2 = new Civilian(coordinatesToTile(settlerX + 1, settlerY + 1), civilizations.get(i), UnitType.WORKER);
//                civilizations.get(i).getUnits().add(civilian2);
//                coordinatesToTile(settlerX + 1, settlerY + 1).setCivilian(civilian2);
//                civilian2.setRemainedCost(0);
//            }


        }

    }

    public Tile coordinatesToTile(int x, int y) {
        if (x < this.x && y < this.y && y >= 0 && x >= 0) return tiles[x][y];
        return null;
    }

    public boolean isInRange(int range, Tile center, Tile destination) {
        HashMap<Tile, Boolean> isVisitedEver = new HashMap<>();
        ArrayList<Tile>[] visited = new ArrayList[range + 1];
        HashMap<Integer, HashMap<Tile, BestMoveClass>> visitedWithMove = new HashMap<>();
        for (int i = 0; i < range + 1; i++) {
            visited[i] = new ArrayList<>();
            visitedWithMove.put(i, new HashMap<>());
        }
        visitedWithMove.get(0).put(center,
                new BestMoveClass(1,
                        null, 0));
        visited[0].add(center);
        isVisitedEver.put(center, true);
        Tile check;
        for (int i = 0; i < range; i++) {
            for (int j = 0; j < visited[i].size() &&
                    visitedWithMove.get(i).get(visited[i].get(j)).movePoint > 0; j++) {
                for (int k = 0; k < 6; k++) {
                    check = visited[i].get(j).getNeighbours(k);
                    int remainingMP = visitedWithMove.get(i).get(visited[i].get(j)).movePoint;
                    if (check == null) break;
                    if (!visitedWithMove.get(i).containsKey(check)) {
                        visitedWithMove.get(i).put(check,
                                new BestMoveClass(remainingMP - 1,
                                        visited[i].get(j), i));
                        visited[i].add(check);
                    } else if (visitedWithMove.get(i).get(check).movePoint < 0) {
                        visitedWithMove.get(i).remove(check);
                        visitedWithMove.get(i).put(check,
                                new BestMoveClass(0,
                                        visited[i].get(j), i));
                    }
                }
            }
            for (int cc = 0; cc < visited[i].size(); cc++) {
                if (!isVisitedEver.containsKey(visited[i].get(cc))) {
                    isVisitedEver.put(visited[i].get(cc), true);
                }
                visitedWithMove.get(i + 1).put(visited[i].get(cc),
                        new BestMoveClass(1,
                                visitedWithMove.get(i).get(visited[i].get(cc)).lastTile, i));
                visited[i + 1].add(visited[i].get(cc));
            }
        }
        return isVisitedEver.containsKey(destination);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private void GenerateMap(ArrayList<Civilization> civilizations) {
        System.out.println("x: " + x + " y: " + y);
        tiles = new Tile[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                tiles[i][j] = new Tile(randomTile(i, j), i, j);
                setNeighborsOfTile(tiles, i, j);
                Random random2 = new Random();
                if (tiles[i][j].getTileType() != TileType.OCEAN &&
                        tiles[i][j].getTileType() != TileType.MOUNTAIN &&
                        tiles[i][j].getCivilization() == null &&
                        tiles[i][j].getCivilian() == null &&
                        tiles[i][j].getNonCivilian() == null &&
                        random2.nextInt(600) % 300 == 0)
                    tiles[i][j].setRuins(new Ruins(random2.nextInt(40) % 5, tiles[i][j]));
            }
        }

        addRiver(5 + x / 16 + random.nextInt(x / 16));
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                setFeature(i, j);
            }
        }
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                setResources(i, j);
            }
        }
    }

    private void setResources(int i, int j) {
        ResourcesTypes resourcesTypes = ResourcesTypes.randomResource();
        while (!tiles[i][j].isResourceTypeValid(resourcesTypes)
                && (tiles[i][j].getTileType().resourcesTypes.length != 0
                || (tiles[i][j].getContainedFeature() != null &&
                tiles[i][j].getContainedFeature().getFeatureType().resourcesTypes.length != 0))) {
            resourcesTypes = ResourcesTypes.randomResource();
        }
        if (random.nextInt(8) != 0) {
            tiles[i][j].setResource(resourcesTypes);
        }
    }

    private void setFeature(int i, int j) {
        FeatureType featureType = FeatureType.randomFeature();
        int k;
        for (k=0;k < 13 && !tiles[i][j].isFeatureTypeValid(featureType); k++ ) {
            featureType = FeatureType.randomFeature();
        }
        if (random.nextInt(4) != 0 && k < 12 && tiles[i][j].getTileType() != TileType.OCEAN) {
            tiles[i][j].setContainedFeature(new Feature(featureType));
        }
    }

    private void addRiver(int number) {
        for (int i = 0; i < number; i++) {
            int length = 5 + random.nextInt(5);
            int startX = 2 + random.nextInt(x - 4);
            int startY = 2 + random.nextInt(y - 4);
            while (tiles[startX][startY].getTileType() == TileType.OCEAN) {
                startX = 2 + random.nextInt(x - 4);
                startY = 2 + random.nextInt(y - 4);
            }
            Tile[] riverSides = new Tile[2];
            Tile[] lastRiverSides = new Tile[2];
            riverSides[0] = tiles[startX][startY];
            riverSides[1] = tiles[startX][startY];
            int neighbour;
            int remainingLength = length;
            mainWhile:
            while (remainingLength > 0) {
                lastRiverSides[0] = riverSides[0];
                lastRiverSides[1] = riverSides[1];
                riverSides = new Tile[2];
                int riverNumber = 0;
                for (int j = 0; j < 6; j++)
                    if (lastRiverSides[0].isRiverWithNeighbour(j)) riverNumber++;
                int riverNumber1 = 0;
                for (int j = 0; j < 6; j++)
                    if (lastRiverSides[1].isRiverWithNeighbour(j)) riverNumber1++;
                if (riverNumber > riverNumber1 + 1) riverSides[0] = lastRiverSides[1];
                else if (1 + riverNumber < riverNumber1) riverSides[0] = lastRiverSides[0];
                else riverSides[0] = lastRiverSides[random.nextInt(2)];
                neighbour = random.nextInt(6);
                int breaker = 0;
                while (riverSides[0].getNeighbours(neighbour) == null ||
                        riverSides[0].isRiverWithNeighbour(neighbour) ||
                        riverSides[0].getNeighbours(neighbour).getTileType() == TileType.OCEAN ||
                        (!riverSides[0].isRiverWithNeighbour((neighbour + 1) % 6) &&
                                !riverSides[0].isRiverWithNeighbour((neighbour - 1) % 6) &&
                                length != remainingLength)) {
                    neighbour = (neighbour + 1) % 6;
                    breaker++;
                    if (breaker > 7)
                        break mainWhile;
                }
                riverSides[1] = riverSides[0].getNeighbours(neighbour);
                riverSides[0].setTilesWithRiver(neighbour);
                riverSides[1].setTilesWithRiver((neighbour + 3) % 6);
                remainingLength--;
            }
        }
    }

    private TileType randomTile(int i, int j) {
        TileType type = TileType.randomTile();
        int distanceFromBoarder = (int) Math.sqrt((x - i) ^ 2 + (y - j) ^ 2);
        while (y / 10 - distanceFromBoarder >= 0 &&
                hasNeighborWithType(i, j, TileType.OCEAN)) {
            if (type == TileType.OCEAN) return type;
            type = TileType.randomTile();
            distanceFromBoarder++;
        }
        int multiple = 2;
        if (hasNeighborWithType(i, j, TileType.SNOW)) multiple++;
        if (i < x / 10 || i + x / 10 > x) {
            while (multiple > 0) {
                if (TileType.SNOW == type) return type;
                type = TileType.randomTile();
                multiple--;
            }
        }
        int rand;
        while (true) {
            rand = random.nextInt(4);
            if (hasNeighborWithType(i, j, type)) rand++;
            if (rand >= 2 && type != TileType.OCEAN && type != TileType.SNOW) break;
            type = TileType.randomTile();
        }
        return type;

    }

    private boolean hasNeighborWithType(int i, int j, TileType tileType) {
        if (i == 0 || j == 0 || i == x - 1 || j == y - 1) return true;
        if (j % 2 == 0) {
            return (tiles[i - 1][j - 1].getTileType() == tileType) ||
                    (tiles[i - 1][j].getTileType() == tileType) ||
                    (tiles[i - 1][j + 1].getTileType() == tileType) ||
                    (tiles[i][j - 1].getTileType() == tileType);
        } else {
            return (tiles[i - 1][j].getTileType() == tileType) ||
                    (tiles[i][j - 1].getTileType() == tileType);
        }
    }

    private void setNeighborsOfTile(Tile[][] tiles, int i, int j) {
        if (i > 0) {
            tiles[i][j].setNeighbours(1, tiles[i - 1][j]);
            tiles[i - 1][j].setNeighbours(4, tiles[i][j]);
        }
        if (i > 0 && j > 0 && j % 2 == 0) {
            tiles[i][j].setNeighbours(0, tiles[i - 1][j - 1]);
            tiles[i - 1][j - 1].setNeighbours(3, tiles[i][j]);
        }
        if (i > 0 && j < y - 1 && j % 2 == 0) {
            tiles[i][j].setNeighbours(2, tiles[i - 1][j + 1]);
            tiles[i - 1][j + 1].setNeighbours(5, tiles[i][j]);
        }
        if (i > 0 && j > 0 && j % 2 == 0) {
            tiles[i][j].setNeighbours(5, tiles[i][j - 1]);
            tiles[i][j - 1].setNeighbours(2, tiles[i][j]);
        }
        if (j % 2 == 1) {
            tiles[i][j].setNeighbours(0, tiles[i][j - 1]);
            tiles[i][j - 1].setNeighbours(3, tiles[i][j]);
        }
    }

    private Tile[][] setMapForBestTile(Civilization.TileCondition[][] civilizationMap,MySocketHandler socketHandler) {
        Tile[][] tiles = new Tile[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (civilizationMap[i][j] == null) {
                    tiles[i][j] = new Tile(TileType.HILL, i, j);
                    tiles[i][j].setContainedFeature(new Feature(FeatureType.FOREST));
                } else {
                    tiles[i][j] = civilizationMap[i][j].getOpenedArea();
                    if (!civilizationMap[i][j].getIsClear()) {
                        tiles[i][j].setNonCivilian(null,socketHandler);
                        tiles[i][j].setCivilian(null,socketHandler);
                    }
                }
                setNeighborsOfTile(tiles, i, j);
            }
        }
        return tiles;
    }

    private class FindNextClass {
        boolean isOver = false;
        boolean foundDestination = false;
        int finalTurn = -10;
    }

    private boolean zoneOfControl(Tile check, Civilization civilization) {
        for (int i = 0; i < 6; i++) {
            if (check.getNeighbours(i) != null &&
                    check.getNeighbours(i).getNonCivilian() != null
                    && check.getNeighbours(i).getNonCivilian().getCivilization() != civilization)
                return true;
        }
        return false;
    }

    private void findNextTileWhile(ArrayList<Tile>[] visited,
                                   HashMap<Integer, HashMap<Tile, BestMoveClass>> visitedWithMove,
                                   Unit unit, int c, Tile destinationTile,
                                   HashMap<Tile, Boolean> isVisitedEver,
                                   FindNextClass findNextClass,
                                   Civilization civilization) {
        Tile check;
        while (!findNextClass.isOver && !findNextClass.foundDestination) {
            findNextClass.isOver = true;
            for (int i = 0; i < visited[c].size(); i++) {
                if (visitedWithMove.get(c).get(visited[c].get(i)).movePoint < 0) continue;
                for (int j = 0; j < 6; j++) {
                    check = visited[c].get(i).getNeighbours(j);
                    if (check != null) {
                        if (isVisitedEver.containsKey(check) ||
                                check.getMovingPrice() > 123)
                            continue;
                        int remainingMP =
                                visitedWithMove.get(c).get(visited[c].get(i)).movePoint;
                        if (unit.getUnitType() == UnitType.SCOUT) remainingMP -= 1;
                        else {
                            if (check.getRoad() != null &&
                                    check.getRoad().getRemainedCost() == 0) {
                                if (check.getRoad().getImprovementType() == ImprovementType.ROAD) {
                                    remainingMP -= (2 * check.getMovingPrice() / 3);
                                } else {
                                    remainingMP -= (check.getMovingPrice() / 2);
                                }
                            } else {
                                remainingMP -= check.getMovingPrice();
                            }
                        }
                        if (remainingMP < 0 ||
                                (visited[c].get(i).isRiverWithNeighbour(j) && check.getRoad() == null)
                                || zoneOfControl(check, civilization))
                            remainingMP = 0;
                        if (!visitedWithMove.get(c).containsKey(check)) {
                            visitedWithMove.get(c).put(check,
                                    new BestMoveClass(remainingMP, visited[c].get(i), c));
                            visited[c].add(check);
                            findNextClass.isOver = false;
                        } else if (visitedWithMove.get(c).get(check).movePoint < remainingMP) {
                            visitedWithMove.get(c).remove(check);
                            visitedWithMove.get(c).put(check,
                                    new BestMoveClass(remainingMP, visited[c].get(i), c));
                            findNextClass.isOver = false;
                        }
                        if (check == destinationTile) {
                            findNextClass.finalTurn = c;
                            findNextClass.foundDestination = true;
                        }
                    }
                }
            }
        }
    }

    private void findNextTileFor(Civilization civilization,
                                 int mp, boolean isCivilian,
                                 int c, ArrayList<Tile>[] visited,
                                 HashMap<Tile, Boolean> isVisitedEver,
                                 FindNextClass findNextClass,
                                 HashMap<Integer, HashMap<Tile, BestMoveClass>> visitedWithMove) {
        for (int i = 0; i < visited[c].size(); i++) {
            if (!isVisitedEver.containsKey(visited[c].get(i))) {
                isVisitedEver.put(visited[c].get(i), true);
                findNextClass.isOver = false;
            }
            if (c < 9 && visitedWithMove.get(c).get(visited[c].get(i)).movePoint == 0 &&
                    ((visited[c].get(i).getCivilian() == null && isCivilian &&
                            (visited[c].get(i).getNonCivilian() == null ||
                                    visited[c].get(i).getNonCivilian().getCivilization() == civilization)) ||
                            (visited[c].get(i).getNonCivilian() == null && !isCivilian))) {
                visitedWithMove.get(c + 1).put(visited[c].get(i),
                        new BestMoveClass(mp, visitedWithMove.get(c).get(visited[c].get(i)).lastTile, c));
                visited[c + 1].add(visited[c].get(i));

            }
        }
    }

    public TileAndMP[] findNextTile(Civilization civilization,
                                    Tile startTile, int remainedMP,
                                    int mp, Tile endTile,
                                    boolean isCivilian, Unit unit,MySocketHandler socketHandler) {
        Tile[][] tiles = setMapForBestTile(civilization.getTileConditions(),socketHandler);
        Tile tile = tiles[startTile.getX()][startTile.getY()];
        Tile destinationTile = tiles[endTile.getX()][endTile.getY()];
        HashMap<Tile, Boolean> isVisitedEver = new HashMap<>();
        ArrayList<Tile>[] visited = new ArrayList[10];
        HashMap<Integer, HashMap<Tile,
                BestMoveClass>> visitedWithMove = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            visited[i] = new ArrayList<>();
            visitedWithMove.put(i, new HashMap<>());
        }
        visitedWithMove.get(0).put(tile,
                new BestMoveClass(remainedMP, null, -1));
        visited[0].add(tile);
        isVisitedEver.put(tile, true);
        FindNextClass findNextClass = new FindNextClass();
        if ((destinationTile.getNonCivilian() != null && !isCivilian)
                || (destinationTile.getCivilian() != null && isCivilian))
            return null;
        for (int c = 0; !findNextClass.isOver &&
                !findNextClass.foundDestination && c < 10; c++) {
            findNextTileWhile(visited, visitedWithMove, unit, c,
                    destinationTile, isVisitedEver, findNextClass, civilization);
            findNextTileFor(civilization, mp, isCivilian, c, visited,
                    isVisitedEver, findNextClass, visitedWithMove);
        }
        if (findNextClass.finalTurn != -10) {
            TileAndMP[] tileAndMPS = new TileAndMP[100];
            Tile current = destinationTile;
            BestMoveClass bestMoveClass =
                    visitedWithMove.get(findNextClass.finalTurn).get(current);
            int thisTurn = findNextClass.finalTurn;
            tileAndMPS[0] = new TileAndMP(bestMoveClass.movePoint,
                    this.tiles[current.getX()][current.getY()]);
            int k = 1;
            for (int i = 0; i <= findNextClass.finalTurn; i++) {
                while (thisTurn == visitedWithMove.get(thisTurn).get(current).turn) {
                    current = visitedWithMove.get(thisTurn).get(current).lastTile;
                    bestMoveClass = visitedWithMove.get(thisTurn).get(current);
                    tileAndMPS[k] = new TileAndMP(bestMoveClass.movePoint,
                            this.tiles[current.getX()][current.getY()]);
                    k++;
                }
                thisTurn--;
            }
            tileAndMPS[k - 1] = null;
            return tileAndMPS;
        }
        return null;
    }

    private Color initColor(int i, int j,
                            Civilization.TileCondition[][] tileConditions,
                            int number) {
        if (i < x && j < y && tileConditions[i][j] != null &&
                tileConditions[i][j].getOpenedArea().isRiverWithNeighbour(number))
            return Color.BLUE_BOLD_BRIGHT;
        return Color.RESET;
    }

    private Color initCurrentTileColor(int i, int j, Color backReset,
                                       Civilization.TileCondition[][] tileConditions) {
        if (i < x && j < y)
            return setBackgroundColor(tileConditions[i][j]);
        return backReset;
    }

    private Color initRightTileColor(Color backReset,
                                     Civilization.TileCondition[][] tileConditions,
                                     int i, int j, boolean isBiggerThan4) {
        if (isBiggerThan4) {
            if (j >= y - 1 || i + j % 2 >= x) return backReset;
            return setBackgroundColor(tileConditions[i + j % 2][j + 1]);
        }
        if (i + j % 2 < 1 || j >= y - 1 || i - 1 + j % 2 >= x || i < 0)
            return backReset;
        return setBackgroundColor(tileConditions[i - 1 + j % 2][j + 1]);
    }

    public String printMap(Civilization.TileCondition[][] tileConditions,
                           int originX, int originY) {
        StringBuilder mapString = new StringBuilder();
        Color backReset = Color.BLACK_BACKGROUND;
        mapString.append("   _____        ".repeat(WINDOW_Y / 2)).append("\n");
        for (int i = originX; i < originX + WINDOW_X; i++) {
            firstFor(originY, i, backReset, tileConditions, mapString);
            secondFor(originY, i, backReset, tileConditions, mapString);
            thirdFor(originY, tileConditions, i, backReset, mapString);
            fourthFor(originY, tileConditions, i, backReset, mapString);
            fifthFor(tileConditions, i, originY, backReset, mapString);
            sixthFor(originY, i, backReset, tileConditions, mapString);
        }
        return mapString.toString();
    }

    private void firstFor(int originY, int i, Color backReset,
                          Civilization.TileCondition[][] tileConditions,
                          StringBuilder mapString) {
        Color color0;
        Color color2;
        String iString;
        String jString;
        String cString;
        String openString;
        for (int j = originY; j < originY + WINDOW_Y; j += 2) {
            Color currentTileColor = initCurrentTileColor(i, j,
                    backReset, tileConditions);
            Color rightTileColor = initRightTileColor(backReset,
                    tileConditions, i, j, false);
            color0 = initColor(i, j, tileConditions, 0);
            color2 = initColor(i, j, tileConditions, 2);
            if ((i + j % 2 - 1 < 0 || j >= y - 1 || i - 1 + j % 2 >= x) ||
                    tileConditions[i - 1 + j % 2][j + 1] == null ||
                    !tileConditions[i - 1 + j % 2][j + 1].getIsClear() ||
                    tileConditions[i - 1 + j % 2][j + 1].getOpenedArea().getCivilian() == null)
                iString = "  ";
            else if (tileConditions[i - 1 + j % 2][j + 1]
                    .getOpenedArea().getCivilian().getCivilization()
                    != tileConditions[i - 1 + j % 2][j + 1]
                    .getOpenedArea().getCivilization())
                iString = Color.RESET.toString()
                        + rightTileColor
                        + Color.getColorByNumber(tileConditions[i - 1 + j % 2][j + 1]
                        .getOpenedArea().getCivilian().getCivilization().getColor()).toString()
                        + tileConditions[i - 1 + j % 2][j + 1]
                        .getOpenedArea().getCivilian().getUnitType().icon
                        + Color.RESET + rightTileColor;
            else
                iString = Color.RESET.toString()
                        + rightTileColor
                        + tileConditions[i - 1 + j % 2][j + 1]
                        .getOpenedArea().getCivilian().getUnitType().icon
                        + Color.RESET + rightTileColor;
            boolean condition = (i + j % 2 < 1 || j >= y - 1 || i - 1 + j % 2 >= x) ||
                    tileConditions[i - 1 + j % 2][j + 1] == null
                    || !tileConditions[i - 1 + j % 2][j + 1].getIsClear();
            if (condition || tileConditions[i - 1 + j % 2][j + 1]
                    .getOpenedArea().getNonCivilian() == null)
                jString = "   ";
            else if (tileConditions[i - 1 + j % 2][j + 1]
                    .getOpenedArea().getNonCivilian().getCivilization()
                    != tileConditions[i - 1 + j % 2][j + 1]
                    .getOpenedArea().getCivilization())
                jString = Color.RESET.toString()
                        + rightTileColor
                        + Color.getColorByNumber(tileConditions[i - 1 + j % 2][j + 1]
                        .getOpenedArea().getNonCivilian().getCivilization().getColor()).toString()
                        + tileConditions[i - 1 + j % 2][j + 1]
                        .getOpenedArea().getNonCivilian().getUnitType().icon
                        + Color.RESET + rightTileColor;
            else
                jString = Color.RESET.toString()
                        + rightTileColor
                        + tileConditions[i - 1 + j % 2][j + 1]
                        .getOpenedArea().getNonCivilian().getUnitType().icon
                        + Color.RESET + rightTileColor;
            if (i < x && j < y && tileConditions[i][j] != null && tileConditions[i][j]
                    .getOpenedArea().getContainedFeature() != null)
                cString = tileConditions[i][j]
                        .getOpenedArea().getContainedFeature().getFeatureType().icon;
            else cString = "  ";
            if (condition) openString = " ";
            else openString = ",";
            mapString.append("  ").append(Color.RESET).append(color0).append("/").
                    append(Color.RESET).append(currentTileColor).append(" ")
                    .append(cString).append("  ").append(Color.RESET).append(color2).append("\\")
                    .append(Color.RESET).append(rightTileColor).append(" ")
                    .append(iString).append(openString).append(jString);
        }
        mapString.append(Color.RESET).append("\n");
    }

    private void secondFor(int originY, int i, Color backReset,
                           Civilization.TileCondition[][] tileConditions,
                           StringBuilder mapString) {
        Color color0;
        Color color2;
        String iString;
        String jString;
        String cString;
        String openString;
        for (int j = originY; j < originY + WINDOW_Y; j += 2) {
            Color currentTileColor = initCurrentTileColor(i, j,
                    backReset, tileConditions);
            Color rightTileColor = initRightTileColor(backReset,
                    tileConditions, i, j, false);
            color0 = initColor(i, j, tileConditions, 0);
            color2 = initColor(i, j, tileConditions, 2);
            if (i + j % 2 > 0 && j < y - 1 && i < x &&
                    tileConditions[i - 1 + (j % 2)][j + 1] != null)
                iString = tileConditions[i - 1 + (j % 2)][j + 1].getOpenedArea().getTileType().icon;
            else iString = "   ";
            if (i < x && j < y && tileConditions[i][j] != null &&
                    tileConditions[i][j].getOpenedArea().getResource() != null)
                cString = tileConditions[i][j].getOpenedArea().getResource().icon;
            else cString = "  ";
            if (i < x && j < y && tileConditions[i][j] != null &&
                    tileConditions[i][j].getOpenedArea().getCity() != null)
                jString = "C ";
            else if (i < x && j < y && tileConditions[i][j] != null &&
                    tileConditions[i][j].getOpenedArea().getImprovement() != null &&
                    tileConditions[i][j].getOpenedArea().getImprovement().getRemainedCost() == 0)
                jString = tileConditions[i][j].getOpenedArea().getImprovement().getImprovementType().icon;
            else jString = "  ";
            if (i < x && j < y && tileConditions[i][j] != null &&
                    tileConditions[i][j].getOpenedArea().getRoad() != null &&
                    tileConditions[i][j].getOpenedArea().getRoad().getRemainedCost() == 0) {
                openString = tileConditions[i][j].getOpenedArea().getRoad().getImprovementType().icon;
            } else openString = " ";
            mapString.append(" ").append(Color.RESET).append(color0).append("/").
                    append(Color.RESET).append(currentTileColor)
                    .append(cString).append(",").append(jString).append(openString)
                    .append(" ").append(Color.RESET).append(color2).append("\\").append(Color.RESET)
                    .append(rightTileColor).append("  ").append(iString).append(" ");
        }
        mapString.append(Color.RESET).append("\n");

    }

    private void thirdFor(int originY,
                          Civilization.TileCondition[][] tileConditions,
                          int i, Color backReset, StringBuilder mapString) {
        Color color0;
        Color color1;
        Color color2;
        String iString;
        String jString;
        for (int j = originY; j < originY + WINDOW_Y; j += 2) {
            Color currentTileColor = initCurrentTileColor(i, j,
                    backReset, tileConditions);
            Color rightTileColor = initRightTileColor(backReset,
                    tileConditions, i, j, false);
            color0 = initColor(i, j, tileConditions, 0);
            if (j % 2 == 1) color1 = initColor(i, j + 1, tileConditions, 4);
            else color1 = initColor(i, j + 1, tileConditions, 1);
            if (color1 == Color.RESET) color1 = rightTileColor;
            color2 = initColor(i, j, tileConditions, 2);
            if (i >= 10) iString = "  " + i;
            else iString = "   " + i;
            if (j >= 10) jString = j + "  ";
            else jString = j + "   ";
            mapString.append(color0).append("/").
                    append(Color.RESET).append(currentTileColor)
                    .append(iString).append(",")
                    .append(jString).append(Color.RESET).append(color2).append("\\")
                    .append(Color.RESET)
                    .append(rightTileColor).append(color1).append("_____")
                    .append(Color.RESET);
        }
        mapString.append("\n");

    }

    private void fourthFor(int originY,
                           Civilization.TileCondition[][] tileConditions,
                           int i, Color backReset, StringBuilder mapString) {
        Color color0;
        Color color2;
        String iString;
        String jString;
        String cString;
        String openString;
        Color rightTileColor;
        for (int j = originY; j < originY + WINDOW_Y; j += 2) {
            Color currentTileColor = initCurrentTileColor(i, j,
                    backReset, tileConditions);
            rightTileColor = initRightTileColor(backReset,
                    tileConditions, i, j, true);
            color0 = initColor(i, j, tileConditions, 5);
            color2 = initColor(i, j, tileConditions, 3);
            if (i >= x || j >= y || tileConditions[i][j] == null || !tileConditions[i][j].getIsClear() ||
                    tileConditions[i][j].getOpenedArea().getCivilian() == null) iString = "  ";
            else if (tileConditions[i][j].getOpenedArea().getCivilian().getCivilization()
                    != tileConditions[i][j].getOpenedArea().getCivilization())
                iString = Color.RESET.toString() + currentTileColor
                        + Color.getColorByNumber(tileConditions[i][j]
                        .getOpenedArea().getCivilian().getCivilization().getColor()).toString()
                        + tileConditions[i][j].getOpenedArea().getCivilian().getUnitType().icon
                        + Color.RESET + currentTileColor;
            else
                iString = Color.RESET.toString() + currentTileColor + tileConditions[i][j]
                        .getOpenedArea().getCivilian().getUnitType().icon + Color.RESET + currentTileColor;
            if (i >= x || j >= y ||
                    tileConditions[i][j] == null ||
                    !tileConditions[i][j].getIsClear() ||
                    tileConditions[i][j].getOpenedArea().getNonCivilian() == null) jString = "    ";
            else if (tileConditions[i][j].getOpenedArea().getNonCivilian().getCivilization()
                    != tileConditions[i][j].getOpenedArea().getCivilization())
                jString = Color.RESET.toString() + currentTileColor
                        + Color.getColorByNumber(tileConditions[i][j]
                        .getOpenedArea().getNonCivilian().getCivilization().getColor()).toString()
                        + tileConditions[i][j].getOpenedArea().getNonCivilian().getUnitType().icon
                        + " " + Color.RESET + currentTileColor;
            else
                jString = Color.RESET.toString() + currentTileColor
                        + tileConditions[i][j]
                        .getOpenedArea().getNonCivilian().getUnitType().icon
                        + " " + Color.RESET + currentTileColor;

            if (j < y - 1 && i + j % 2 < x && tileConditions[i + j % 2][j + 1]
                    != null && tileConditions[i + j % 2][j + 1]
                    .getOpenedArea().getContainedFeature() != null)
                cString = tileConditions[i + j % 2][j + 1]
                        .getOpenedArea().getContainedFeature().getFeatureType().icon;
            else cString = "  ";
            if (i >= x || j >= y || tileConditions[i][j] == null
                    || !tileConditions[i][j].getIsClear())
                openString = " ";
            else openString = ",";
            mapString.append(Color.RESET).append(color0).append("\\")
                    .append(Color.RESET).append(currentTileColor).append(" ")
                    .append(iString).append(openString).append(jString).append(" ").append(Color.RESET)
                    .append(color2).append("/")
                    .append(Color.RESET)
                    .append(rightTileColor).append(" ")
                    .append(cString).append("  ");
        }
        mapString.append(Color.RESET).append("\n");

    }

    private void fifthFor(Civilization.TileCondition[][] tileConditions,
                          int i, int originY, Color backReset,
                          StringBuilder mapString) {
        Color color0;
        Color color2;
        String iString;
        String jString;
        String cString;
        String openString;
        Color rightTileColor;
        for (int j = originY; j < originY + WINDOW_Y; j += 2) {
            Color currentTileColor = initCurrentTileColor(i, j, backReset, tileConditions);
            rightTileColor = initRightTileColor(backReset, tileConditions, i, j, true);
            color0 = initColor(i, j, tileConditions, 5);
            color2 = initColor(i, j, tileConditions, 3);
            if (i < x && j < y && tileConditions[i][j] != null)
                iString = tileConditions[i][j].getOpenedArea().getTileType().icon;
            else iString = "   ";
            if (j < y - 1 && i + j % 2 < x && tileConditions[i + j % 2][j + 1] != null &&
                    tileConditions[i + j % 2][j + 1].getOpenedArea().getResource() != null)
                cString = tileConditions[i + j % 2][j + 1].getOpenedArea().getResource().icon;
            else cString = "  ";
            if (j < y - 1 && i + j % 2 < x && tileConditions[i + j % 2][j + 1] != null &&
                    tileConditions[i + j % 2][j + 1].getOpenedArea().getCity() != null)
                jString = "C ";
            else if (j < y - 1 && i + j % 2 < x && tileConditions[i + j % 2][j + 1] != null &&
                    tileConditions[i + j % 2][j + 1].getOpenedArea().getImprovement() != null &&
                    tileConditions[i + j % 2][j + 1].getOpenedArea().getImprovement().getRemainedCost() == 0)
                jString = tileConditions[i + j % 2][j + 1]
                        .getOpenedArea().getImprovement().getImprovementType().icon;
            else jString = "  ";
            if (j < y - 1 && i + j % 2 < x && tileConditions[i + j % 2][j + 1] != null &&
                    tileConditions[i + j % 2][j + 1].getOpenedArea().getRoad() != null &&
                    tileConditions[i + j % 2][j + 1].getOpenedArea().getRoad().getRemainedCost() == 0) {
                openString = tileConditions[i + j % 2][j + 1]
                        .getOpenedArea().getRoad().getImprovementType().icon;
            } else openString = " ";
            mapString.append(" ").append(Color.RESET).append(color0).append("\\")
                    .append(Color.RESET).append(currentTileColor).append("  ")
                    .append(iString)
                    .append("  ").append(Color.RESET).append(color2).append("/")
                    .append(Color.RESET).append(rightTileColor)
                    .append(cString).append(",").append(jString).append(openString);
        }
        mapString.append(Color.RESET).append("\n");

    }

    private void sixthFor(int originY, int i, Color backReset,
                          Civilization.TileCondition[][] tileConditions,
                          StringBuilder mapString) {
        Color color0;
        Color color1;
        Color color2;
        String iString;
        String jString;
        Color rightTileColor;
        for (int j = originY; j < originY + WINDOW_Y; j += 2) {
            Color currentTileColor = initCurrentTileColor(i, j,
                    backReset, tileConditions);
            if (j >= y - 1 || i + j % 2 >= x) rightTileColor = backReset;
            else rightTileColor =
                    setBackgroundColor(tileConditions[i + j % 2][j + 1]);
            color0 = initColor(i, j, tileConditions, 5);
            color1 = initColor(i, j, tileConditions, 4);
            color2 = initColor(i, j, tileConditions, 3);
            if (color1 == Color.RESET) color1 = currentTileColor;
            if (i + j % 2 >= 10) iString = "  " + (i + j % 2);
            else iString = "   " + (i + j % 2);
            if (j + 1 >= 10) jString = (j + 1) + "";
            else jString = (j + 1) + " ";
            mapString.append("  ").append(Color.RESET).append(color0).append("\\").
                    append(Color.RESET).append(currentTileColor).append(color1).append("_____")
                    .append(Color.RESET).append(color2).append("/").append(Color.RESET)
                    .append(rightTileColor).append(iString).append(",").append(jString);
        }
        mapString.append(Color.RESET).append("\n");
    }

    private  Color setBackgroundColor(Civilization.TileCondition tileCondition) {
        if (tileCondition == null || !tileCondition.getIsClear())
            return Color.WHITE_BACKGROUND;
        else if (tileCondition.getOpenedArea().getCivilization() == null)
            return Color.BLACK_BACKGROUND_BRIGHT;
        else return Color.getBackgroundColorWithNumber
                    (tileCondition.getOpenedArea().getCivilization().getColor());
    }

    private  class BestMoveClass {
        int movePoint;
        Tile lastTile;
        int turn;

        BestMoveClass(int movePoint,
                      Tile lastTile, int turn) {
            this.lastTile = lastTile;
            this.movePoint = movePoint;
            this.turn = turn;
        }
    }

    public  class TileAndMP {
        public int movePoint;
        public Tile tile;

        public TileAndMP(int movePoint, Tile tile) {
            this.movePoint = movePoint;
            this.tile = tile;
        }
    }

    public  void setX(int x) {
        this.x = x;
    }

    public  void setY(int y) {
        this.y = y;
    }

    public Tile randomTile() {
        Random random = new Random();
        return tiles[random.nextInt(x)][random.nextInt(y)];
    }
}




