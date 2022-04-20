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

    private  Tile[][] tiles;
    private int x;
    private int y;
    private Random random = new Random();
    public Map(ArrayList<Civilization> civilizations)
    {
        GenerateMap(civilizations);
        for (Civilization civilization : civilizations) civilization.setOpenedArea(x, y);
    }


    public Tile coordinatesToTile(int x, int y) {
        if(x < this.x && y < this.y && y >= 0 && x >=0 ) return tiles[x][y];
        return null;
    }

    public boolean isTileValid(Unit unit)
    {
        return false;
    }
    public boolean isRangeValid(Unit unit, Tile tile)
    {
        return false;
    }
    private void GenerateMap(ArrayList<Civilization> civilizations)
    {
        setXAndY(civilizations.size());
        tiles = new Tile[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                tiles[i][j] = new Tile(randomTile(i,j),i,j);
                setNeighborsOfTile(i,j);
            }
        }
        addRiver(1 + random.nextInt(4));
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                setFeature(i,j);
            }
        }
    }
    private void setFeature(int i,int j){
        FeatureType featureType = FeatureType.randomFeature();
        while (!tiles[i][j].isFeatureTypeValid(featureType)){
            featureType = FeatureType.randomFeature();
        }
        if(random.nextInt(6) != 0){
            tiles[i][j].setFeature(new Feature(featureType));
        }
    }
    private void addRiver(int number){
        for (int i = 0; i < number; i++) {
            int length = 3 + random.nextInt(6);
            int startX = 2 + random.nextInt(x - 4);
            int startY = 2 + random.nextInt(y - 4);
            while (tiles[startX][startY].getTileType() == TileType.OCEAN){
                startX = 2 + random.nextInt(x - 4);
                startY = 2 + random.nextInt(y - 4);
            }
            Tile[] riverSides = new Tile[2];
            Tile[] lastRiverSides = new Tile[2];
            riverSides[0] = tiles[startX][startY];
            int neighbour = random.nextInt( 6);
            while(riverSides[0].getNeighbours(neighbour) == null ){
                neighbour  = (neighbour+1)%6 ;
            }
            riverSides[1] = tiles[startX][startY].getNeighbours(neighbour);
            riverSides[0].setTilesWithRiver(neighbour);
            riverSides[1].setTilesWithRiver((neighbour + 3) % 6);
            length--;
            while (length > 0){
                lastRiverSides = riverSides;
                riverSides = new Tile[2];
                riverSides[0] = lastRiverSides[random.nextInt(2)];
                neighbour = random.nextInt( 6);
                while(riverSides[0].getNeighbours(neighbour) == null ||
                        (!riverSides[0].isRiverWithNeighbour((neighbour+1)%6) && !riverSides[0].isRiverWithNeighbour((neighbour-1)%6))){
                    neighbour  = (neighbour+1)%6 ;
                }
                riverSides[1] = tiles[startX][startY].getNeighbours(neighbour);
                riverSides[0].setTilesWithRiver(neighbour);
                riverSides[1].setTilesWithRiver((neighbour + 3) % 6);
                length--;
            }
        }
    }
    private TileType randomTile(int i, int j){
        TileType type = TileType.randomTile();
        int distanceFromBoarder = (int) Math.sqrt((x - i)^2 + (y - j)^2);
        while (y/10 - 2 - distanceFromBoarder >= 0 && hasNeighborWithType(i,j,TileType.OCEAN)){
            if (type == TileType.OCEAN) return type;
            type = TileType.randomTile();
            distanceFromBoarder++;
        }
        int mult = 2;
        if (hasNeighborWithType(i,j,TileType.SNOW)) mult++;
        if (i < x/10 || i + x/10 > x ) {
            while (mult >0){
                if(TileType.SNOW == type) return type;
                type = TileType.randomTile();
                mult--;
            }
        }
        int rand;
        while (true){
            rand = random.nextInt(4);
            if (hasNeighborWithType(i,j,type)) rand++;
            if(rand >= 2 && type != TileType.OCEAN && type != TileType.SNOW) break;
            type = TileType.randomTile();
        }
        return type;

    }

    private boolean hasNeighborWithType(int i,int j,TileType tileType){
        if(i == 0 || j == 0 || i == x-1 || j == y-1) return true;
        if((tiles[i][j - 1].getTileType() == tileType) ||
                (tiles[i - 1][j].getTileType() == tileType) ||
                (tiles[i - 1][j - 1 + (2 * (i % 2))].getTileType() == tileType))
            return true;
        return false;
    }

    private void setNeighborsOfTile(int i,int j){
        if(j > 0){
            tiles[i][j].setNeighbours(0,tiles[i][j-1]);
            tiles[i][j-1].setNeighbours(3,tiles[i][j]);
        }
        if(i > 0 && j > 0 && i % 2 == 0){
            tiles[i][j].setNeighbours(1,tiles[i -1][j - 1]);
            tiles[i-1][j - 1].setNeighbours(4,tiles[i][j]);
        }
        if(i > 0 && i % 2 == 1){
            tiles[i][j].setNeighbours(1,tiles[i -1][j]);
            tiles[i-1][j].setNeighbours(4,tiles[i][j]);
        }
        if(i > 0 && i % 2 ==0){
            tiles[i][j].setNeighbours(2,tiles[i -1][j]);
            tiles[i-1][j].setNeighbours(5,tiles[i][j]);
        }
        if(i > 0 && j < y - 1 && i % 2 ==1){
            tiles[i][j].setNeighbours(2,tiles[i -1][j + 1]);
            tiles[i-1][j+ 1].setNeighbours(5,tiles[i][j]);
        }
    }

    private void setXAndY(int civilizationNumber){
        switch (civilizationNumber){
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
    public static Tile FindBestMove(Unit unit)
    {

        return null;
    }
    private  void turnNeeded(int x,int y,int mp,int destX,int destY){
        boolean[][] visitedTile = new boolean[this.x][this.y];
        //while(visitedTile[destX])
    }

    public boolean doTheseHaveRiver(Tile firstTile, Tile secondTile)
    {
        return false;
    }
}

