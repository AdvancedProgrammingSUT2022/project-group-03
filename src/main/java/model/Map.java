package model;

import model.Units.Unit;

import model.features.Feature;
import model.features.FeatureType;
import model.tiles.Tile;
import model.tiles.TileType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Map {

    private Tile[][] tiles;
    private int x;
    private int y;
    private Random random = new Random();

    public Map(ArrayList<Civilization> civilizations) {
        GenerateMap(civilizations);

    }


    public Tile coordinatesToTile(int x, int y) {
        if (x < this.x && y < this.y && y >= 0 && x >= 0) return tiles[x][y];
        return null;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isTileValid(Unit unit) {
        return false;
    }

    public boolean isRangeValid(Unit unit, Tile tile) {
        return false;
    }

    private void GenerateMap(ArrayList<Civilization> civilizations) {
        setXAndY(civilizations.size());
        tiles = new Tile[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                tiles[i][j] = new Tile(randomTile(i, j), i, j);
                setNeighborsOfTile(i, j);
            }
        }

        addRiver(x / 16 + random.nextInt(x / 16));
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                setFeature(i, j);
            }
        }
    }

    private void setFeature(int i, int j) {
        FeatureType featureType = FeatureType.randomFeature();

        while (!tiles[i][j].isFeatureTypeValid(featureType) && tiles[i][j].getTileType().featureTypes.length != 0) {
            featureType = FeatureType.randomFeature();
        }
        if (random.nextInt(6) != 0) {
            tiles[i][j].setFeature(new Feature(featureType));
        }
    }

    private void addRiver(int number) { // TODO: 4/25/2022   gir kardan ba tedad ziad
        int length = 5 + random.nextInt(1);
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
        while (remainingLength > 0) {
            lastRiverSides[0] = riverSides[0];
            lastRiverSides[1] = riverSides[1];
            riverSides = new Tile[2];
            riverSides[0] = lastRiverSides[random.nextInt(2)];
            neighbour = random.nextInt(6);
            while (riverSides[0].getNeighbours(neighbour) == null ||
                    riverSides[0].isRiverWithNeighbour(neighbour) ||
                    riverSides[0].getNeighbours(neighbour).getTileType() == TileType.OCEAN ||
                    (!riverSides[0].isRiverWithNeighbour((neighbour + 1) % 6) &&
                            !riverSides[0].isRiverWithNeighbour((neighbour - 1) % 6) &&
                            length != remainingLength)) {
                neighbour = (neighbour + 1) % 6;
            }
            riverSides[1] = tiles[startX][startY].getNeighbours(neighbour);
            riverSides[0].setTilesWithRiver(neighbour);
            riverSides[1].setTilesWithRiver((neighbour + 3) % 6);
            remainingLength--;
            System.out.println(riverSides[0].getX() + " " + riverSides[0].getY());
        }
    }

    private TileType randomTile(int i, int j) {
        TileType type = TileType.randomTile();
        int distanceFromBoarder = (int) Math.sqrt((x - i) ^ 2 + (y - j) ^ 2);
        while (y / 10 - 2 - distanceFromBoarder >= 0 && hasNeighborWithType(i, j, TileType.OCEAN)) {
            if (type == TileType.OCEAN) return type;
            type = TileType.randomTile();
            distanceFromBoarder++;
        }
        int mult = 2;
        if (hasNeighborWithType(i, j, TileType.SNOW)) mult++;
        if (i < x / 10 || i + x / 10 > x) {
            while (mult > 0) {
                if (TileType.SNOW == type) return type;
                type = TileType.randomTile();
                mult--;
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

    private void setNeighborsOfTile(int i, int j) {
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

    private void setXAndY(int civilizationNumber) {
        switch (civilizationNumber) {
            case 2:
                x = 46;
                y = 74;
                break;
            case 3:
            case 4:
                x = 54;
                y = 84;
                break;
            case 5:
                x = 60;
                y = 90;
                break;
        }
    }

    public TileAndMP[] findNextTile(Tile tile, int mp, Tile destinationTile, boolean isCivilian) {// TODO: 4/25/2022  vaghti resid maghsad naiad biroon
        HashMap<Tile, Boolean> isVisitedEver = new HashMap<>();
        ArrayList<Tile>[] visited = new ArrayList[10];
        HashMap<Integer, HashMap<Tile, BestMoveClass>> visitedWithMove = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            visited[i] = new ArrayList<Tile>();
            visitedWithMove.put(i, new HashMap<>());
        }
        visitedWithMove.get(0).put(tile, new BestMoveClass(mp, null, mp));
        visited[0].add(tile);
        isVisitedEver.put(tile, true);
        Tile check;
        int finalTurn = -10;
        boolean isOver = false;
        loop:
        for (int c = 0; !isOver && c < 10; c++) {
            while (!isOver) {
                isOver = true;
                for (int i = 0; i < visited[c].size() && visitedWithMove.get(c).get(visited[c].get(i)).movePoint > 0; i++) {
                    for (int j = 0; j < 6; j++) {
                        check = visited[c].get(i).getNeighbours(j);
                        if (check != null) {
                            if (isVisitedEver.containsKey(check) ||
                                    check.getTileType() == TileType.OCEAN ||
                                    check.getTileType() == TileType.MOUNTAIN ||
                                    (check.getFeature() != null && check.getFeature().getFeatureType() == FeatureType.ICE))
                                continue;
                            int remainingMP = visitedWithMove.get(c).get(visited[c].get(i)).movePoint - check.getMovingPrice();
                            if (remainingMP < 0 || visited[c].get(i).isRiverWithNeighbour(j)) remainingMP = 0;
                            if (!visitedWithMove.get(c).containsKey(check)) {
                                visitedWithMove.get(c).put(check, new BestMoveClass(remainingMP, visited[c].get(i), c));
                                visited[c].add(check);
                                isOver = false;
                            } else if (visitedWithMove.get(c).get(check).movePoint < remainingMP) {
                                visitedWithMove.get(c).remove(check);
                                visitedWithMove.get(c).put(check, new BestMoveClass(remainingMP, visited[c].get(i), c));
                                isOver = false;
                            }
                            if (check == destinationTile) {
                                finalTurn = c;
                                break loop;
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < visited[c].size(); i++) {
                if (!isVisitedEver.containsKey(visited[c].get(i))) {
                    isVisitedEver.put(visited[c].get(i), true);
                    isOver = false;
                }
                if (c < 9 && visitedWithMove.get(c).get(visited[c].get(i)).movePoint == 0 &&
                        ((visited[c].get(i).getCivilian() == null && isCivilian)
                                || (visited[c].get(i).getNonCivilian() == null && !isCivilian))) {
                    visitedWithMove.get(c + 1).put(visited[c].get(i),
                            new BestMoveClass(mp, visitedWithMove.get(c).get(visited[c].get(i)).lastTile, c));
                    visited[c + 1].add(visited[c].get(i));

                }

            }

        }
        if (finalTurn != -10) {
            TileAndMP[] tileAndMPS = new TileAndMP[finalTurn + 1];
            Tile current = destinationTile;
            BestMoveClass bestMoveClass = visitedWithMove.get(finalTurn).get(current);
            int thisTurn = finalTurn;
            tileAndMPS[0] = new TileAndMP(bestMoveClass.movePoint, current);
            for (int i = 1; i <= finalTurn; i++) {
                while (thisTurn == visitedWithMove.get(thisTurn).get(current).turn) {
                    current = visitedWithMove.get(thisTurn).get(current).lastTile;
                    bestMoveClass = visitedWithMove.get(thisTurn).get(current);
                }
                thisTurn--;
                tileAndMPS[i] = new TileAndMP(bestMoveClass.movePoint, current);
            }
            return tileAndMPS;
        }
        return null;

    }

    public boolean doTheseHaveRiver(Tile firstTile, Tile secondTile) {
        return false;
    }

    // TODO: 4/25/2022   printe rangi unita
    public String printMap(Civilization.TileCondition[][] tileConditions, int originX, int originY) {
        StringBuilder mapString = new StringBuilder();
        Color color0;
        Color color2;
        Color color1;
        String iString;
        String jString;
        String cString;
        Color currentTileColor;
        Color rightTileColor;
        Color backReset = Color.BLACK_BACKGROUND;
        mapString.append("   _____        ".repeat(7)).append("\n");
        for (int i = originX; i < originX + 5; i++) {
            for (int j = originY; j < originY + 14; j += 2) {
                if (i < x && j < y) currentTileColor = setBackgroundColor(tileConditions[i][j]);
                else currentTileColor = backReset;
                if (i + j % 2 - 1 < 0 || j >= y - 1 || i - 1 + j % 2 >= x || i < 0) rightTileColor = backReset;
                else rightTileColor = setBackgroundColor(tileConditions[i - 1 + j % 2][j + 1]);
                if (i < x && j < y && tileConditions[i][j] != null &&
                        tileConditions[i][j].getOpenedArea().isRiverWithNeighbour(0)) color0 = Color.BLUE;
                else color0 = Color.RESET;
                if (i < x && j < y && tileConditions[i][j] != null &&
                        tileConditions[i][j].getOpenedArea().isRiverWithNeighbour(2)) color2 = Color.BLUE;
                else color2 = Color.RESET;
                if ((i + j % 2 - 1 < 0 || j >= y - 1 || i - 1 + j % 2 >= x) || tileConditions[i - 1 + j % 2][j + 1] == null ||
                        tileConditions[i - 1 + j % 2][j + 1].getOpenedArea().getCivilian() == null) iString = "    ";
                else
                    iString = " " + Color.RED + rightTileColor + tileConditions[i - 1 + j % 2][j + 1].getOpenedArea().getCivilian().getUnitType().getIcon() + " " + Color.RESET;
                if ((i + j % 2 < 1 || j >= y - 1 || i - 1 + j % 2 >= x) || tileConditions[i - 1 + j % 2][j + 1] == null ||
                        tileConditions[i - 1 + j % 2][j + 1].getOpenedArea().getNonCivilian() == null) jString = "  ";
                else jString =
                        rightTileColor + " " + Color.getColorByNumber(tileConditions[i - 1 + j % 2][j + 1]
                                .getOpenedArea().getNonCivilian().getCivilization().getColor()).toString() +
                                rightTileColor + tileConditions[i - 1 + j % 2][j + 1].getOpenedArea().getNonCivilian().getUnitType().getIcon() + Color.RESET;
                if (i < x && j < y && tileConditions[i][j] != null && tileConditions[i][j].getOpenedArea().getFeature() != null)
                    cString = tileConditions[i][j].getOpenedArea().getFeature().getFeatureType().icon;
                else cString = "  ";
                mapString.append("  ").append(color0).append("/").
                        append(Color.RESET).append(currentTileColor).append(" ").append(cString).append("  ").append(color2).append("\\")
                        .append(Color.RESET).append(rightTileColor).append(iString).append(",").append(jString);
            }
            mapString.append(Color.RESET).append("\n");
            for (int j = originY; j < originY + 14; j += 2) {
                if (i < x && j < y) currentTileColor = setBackgroundColor(tileConditions[i][j]);
                else currentTileColor = backReset;
                if (i + j % 2 - 1 < 0 || j >= y - 1 || i - 1 + j % 2 >= x || i < 0) rightTileColor = backReset;
                else rightTileColor = setBackgroundColor(tileConditions[i - 1 + j % 2][j + 1]);
                if (i < x && j < y && tileConditions[i][j] != null &&
                        tileConditions[i][j].getOpenedArea().isRiverWithNeighbour(0)) color0 = Color.BLUE;
                else color0 = Color.RESET;
                if (i < x && j < y && tileConditions[i][j] != null &&
                        tileConditions[i][j].getOpenedArea().isRiverWithNeighbour(2)) color2 = Color.BLUE;
                else color2 = Color.RESET;
                if (i + j % 2 > 1 && j < y - 1 && i + 1 < x && tileConditions[i - 1 + (j % 2)][j + 1] != null)
                    iString = tileConditions[i - 1 + (j % 2)][j + 1].getOpenedArea().getTileType().icon;
                else iString = "   ";
                mapString.append(" ").append(color0).append("/").
                        append(Color.RESET).append(currentTileColor)
                        .append("       ").append(color2).append("\\").append(Color.RESET)
                        .append(rightTileColor).append("  ").append(iString).append(" ");
            }
            mapString.append(Color.RESET).append("\n");
            for (int j = originY; j < originY + 14; j += 2) {
                if (i < x && j < y) currentTileColor = setBackgroundColor(tileConditions[i][j]);
                else currentTileColor = backReset;
                if ((i + j % 2 - 1 < 0 || j >= y - 1 || i - 1 + j % 2 >= x || i < 0)) rightTileColor = backReset;
                else rightTileColor = setBackgroundColor(tileConditions[i - 1 + j % 2][j + 1]);
                if (i < x && j < y && tileConditions[i][j] != null &&
                        tileConditions[i][j].getOpenedArea().isRiverWithNeighbour(0)) color0 = Color.BLUE;
                else color0 = Color.RESET;
                if (i < x && j < y - 1 && tileConditions[i][j + 1] != null &&
                        tileConditions[i][j + 1].getOpenedArea().isRiverWithNeighbour(1)) color1 = Color.BLUE;
                else color1 = Color.RESET;
                if (i < x && j < y && tileConditions[i][j] != null &&
                        tileConditions[i][j].getOpenedArea().isRiverWithNeighbour(2)) color2 = Color.BLUE;
                else color2 = Color.RESET;
                if (i >= 10) iString = "  " + i;
                else iString = "   " + i;
                if (j >= 10) jString = j + "  ";
                else jString = j + "   ";
                mapString.append(color0).append("/").
                        append(Color.RESET).append(currentTileColor).append(iString).append(",")
                        .append(jString).append(color2).append("\\")
                        .append(Color.RESET).append(color1).append(rightTileColor).append("_____").append(Color.RESET);
            }
            mapString.append("\n");
            for (int j = originY; j < originY + 14; j += 2) {
                if (i < x && j < y) currentTileColor = setBackgroundColor(tileConditions[i][j]);
                else currentTileColor = backReset;
                if (j >= y - 1 || i + j % 2 >= x || i < 0) rightTileColor = backReset;
                else rightTileColor = setBackgroundColor(tileConditions[i + j % 2][j + 1]);
                if (i < x && j < y && tileConditions[i][j] != null &&
                        tileConditions[i][j].getOpenedArea().isRiverWithNeighbour(5)) color0 = Color.BLUE;
                else color0 = Color.RESET;
                if (i < x && j < y && tileConditions[i][j] != null &&
                        tileConditions[i][j].getOpenedArea().isRiverWithNeighbour(3)) color2 = Color.BLUE;
                else color2 = Color.RESET;
                if (i >= x || j >= y || tileConditions[i][j] == null ||
                        tileConditions[i][j].getOpenedArea().getCivilian() == null) iString = "    ";
                else iString = currentTileColor + " " +
                        Color.getColorByNumber(tileConditions[i][j]
                                .getOpenedArea().getCivilian().getCivilization().getColor()).toString() +
                        currentTileColor + tileConditions[i][j].getOpenedArea().getCivilian().getUnitType().getIcon() + " " + Color.RESET;
                if (i >= x || j >= y || tileConditions[i][j] == null ||
                        tileConditions[i][j].getOpenedArea().getNonCivilian() == null) jString = "    ";
                else jString = Color.getColorByNumber(tileConditions[i][j]
                        .getOpenedArea().getNonCivilian().getCivilization().getColor()).toString() +
                        currentTileColor + " " + tileConditions[i][j].getOpenedArea().getNonCivilian().getUnitType().getIcon() + Color.RESET;
                if (j < y - 1 && i + j % 2 < x && tileConditions[i + j % 2][j + 1] != null && tileConditions[i + j % 2][j + 1].getOpenedArea().getFeature() != null)
                    cString = tileConditions[i + j % 2][j + 1].getOpenedArea().getFeature().getFeatureType().icon;
                else cString = "  ";
                mapString.append(color0).append("\\")
                        .append(Color.RESET).append(currentTileColor).append(iString).append(",").append(jString)
                        .append(color2).append("/")
                        .append(Color.RESET).append(rightTileColor).append(" ").append(cString).append("  ");
            }
            mapString.append(Color.RESET).append("\n");
            for (int j = originY; j < originY + 14; j += 2) {
                if (i < x && j < y) currentTileColor = setBackgroundColor(tileConditions[i][j]);
                else currentTileColor = backReset;
                if (j >= y - 1 || i + j % 2 >= x) rightTileColor = backReset;
                else rightTileColor = setBackgroundColor(tileConditions[i + j % 2][j + 1]);
                if (i < x && j < y && tileConditions[i][j] != null &&
                        tileConditions[i][j].getOpenedArea().isRiverWithNeighbour(5)) color0 = Color.BLUE;
                else color0 = Color.RESET;
                if (i < x && j < y && tileConditions[i][j] != null &&
                        tileConditions[i][j].getOpenedArea().isRiverWithNeighbour(3)) color2 = Color.BLUE;
                else color2 = Color.RESET;
                if (i < x && j < y && tileConditions[i][j] != null)
                    iString = tileConditions[i][j].getOpenedArea().getTileType().icon;
                else iString = "   ";
                mapString.append(" ").append(color0).append("\\")
                        .append(Color.RESET).append(currentTileColor).append("  ")
                        .append(iString)
                        .append("  ").append(color2).append("/")
                        .append(Color.RESET).append(rightTileColor).append("      ");
            }

            mapString.append(Color.RESET).append("\n");
            for (int j = originY; j < originY + 14; j += 2) {
                if (i < x && j < y) currentTileColor = setBackgroundColor(tileConditions[i][j]);
                else currentTileColor = backReset;
                if (j >= y - 1 || i + j % 2 >= x) rightTileColor = backReset;
                else rightTileColor = setBackgroundColor(tileConditions[i + j % 2][j + 1]);
                if (i < x && j < y && tileConditions[i][j] != null &&
                        tileConditions[i][j].getOpenedArea().isRiverWithNeighbour(5)) color0 = Color.BLUE;
                else color0 = Color.RESET;
                if (i < x && j < y && tileConditions[i][j] != null &&
                        tileConditions[i][j].getOpenedArea().isRiverWithNeighbour(4)) color1 = Color.BLUE;
                else color1 = Color.RESET;
                if (i < x && j < y && tileConditions[i][j] != null &&
                        tileConditions[i][j].getOpenedArea().isRiverWithNeighbour(3)) color2 = Color.BLUE;
                else color2 = Color.RESET;

                if (i + j % 2 >= 10) iString = "  " + (i + j % 2);
                else iString = "   " + (i + j % 2);
                if (j + 1 >= 10) jString = (j + 1) + "";
                else jString = (j + 1) + " ";
                mapString.append("  ").append(color0).append("\\").
                        append(Color.RESET).append(color1).append(currentTileColor).append("_____")
                        .append(Color.RESET).append(color2).append("/")
                        .append(rightTileColor).append(iString).append(",").append(jString);
            }
            mapString.append(Color.RESET).append("\n");
        }
        return mapString.toString();
    }


    private static Color setBackgroundColor(Civilization.TileCondition tileCondition) {
        if (tileCondition == null) return Color.WHITE_BACKGROUND;
        else if (tileCondition.getOpenedArea().getCivilization() == null)
            return Color.BLACK_BACKGROUND_BRIGHT;
        else return Color.getBackgroundColorWithNumber(tileCondition.getOpenedArea().getCivilization().getColor());
    }

    private static class BestMoveClass {
        int movePoint;
        Tile lastTile;
        int turn;

        BestMoveClass(int movePoint, Tile lastTile, int turn) {
            this.lastTile = lastTile;
            this.movePoint = movePoint;
            this.turn = turn;
        }
    }

    public static class TileAndMP {
        public int movePoint;
        public Tile tile;

        TileAndMP(int movePoint, Tile tile) {
            this.movePoint = movePoint;
            this.tile = tile;
        }
    }
}



