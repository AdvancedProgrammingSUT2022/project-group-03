package controller.gameController;

import model.*;
import model.Units.*;
import model.features.FeatureType;
import model.improvements.ImprovementType;
import model.technologies.TechnologyType;
import model.tiles.Tile;
import model.tiles.TileType;
import network.GameHandler;


import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Locale;

public class GameController {
    private final GameHandler game;

    public GameController(GameHandler game) {
        this.game = game;
    }
    private ArrayList<Civilization> civilizations = new ArrayList<>();
    private ArrayList<Tasks> unfinishedTasks = new ArrayList<>();
    private int playerTurn = 0;
    private Map map;
     int startWindowX = 0;
     int startWindowY = 0;
    private  int cycle;

    private static Civilization winnerSend;

    public  void startGame(ArrayList<User> PlayersNames) {
        cycle = 1;
        setCivilizations(PlayersNames);
        map = new Map(civilizations);
        for (int i = 0; i < PlayersNames.size(); i++)
            civilizations.get(i).setTileConditions
                    (new Civilization.TileCondition[map.getX()][map.getY()]);
        map.addStartingSettlers(civilizations,game.getSocketHandlers().get(game.getGameController().getPlayerTurn()));

//        for (int i = 0; i < GameController.getCivilizations().size(); i++)
//            nextTurn();
        setUnfinishedTasks();
//        for (Civilization civilization : GameController.getCivilizations()) {
//            if (civilization != GameController.getCivilizations().get(GameController.getPlayerTurn()))
//                GameController.getCivilizations().get(GameController.getPlayerTurn()).getKnownCivilizations().add(new Pair<>(civilization, 0));
//        }
//        GameControllerFX.renderMap(anchorPane);

    }


     City nameToCity(String name) {
        for (Civilization civilization : civilizations)
            for (int j = 0; j < civilization.getCities().size(); j++)
                if (civilization.getCities().get(j).getName()
                        .toLowerCase(Locale.ROOT).equals(name.toLowerCase(Locale.ROOT)))
                    return civilization.getCities().get(j);
        return null;
    }


     Tasks findTask(Tasks tasks) {
        for (Tasks unfinishedTask : unfinishedTasks)
            if (tasks.getTaskTypes() == unfinishedTask.getTaskTypes() &&
                    tasks.getTile() == unfinishedTask.getTile())
                return unfinishedTask;
        return null;
    }

    public  void deleteFromUnfinishedTasks(Tasks task) {
        Tasks gettingDeletedTask = findTask(task);
        if (findTask(task) == null)
            return;
        unfinishedTasks.remove(gettingDeletedTask);
    }

    public  boolean doesHaveTheRequiredTechnologyToBuildImprovement(ImprovementType improvementType,
                                                                          Tile tile, Civilization civilization) {
        if (civilization.doesContainTechnology(improvementType.prerequisitesTechnologies) != 1)
            return false;
        if ((improvementType == ImprovementType.FARM || improvementType == ImprovementType.MINE)
                && (tile.getContainedFeature() != null &&
                tile.getContainedFeature().getFeatureType() == FeatureType.JUNGLE &&
                civilization.doesContainTechnology(TechnologyType.BRONZE_WORKING) != 1))
            return false;
        if (improvementType == ImprovementType.MINE && tile.getContainedFeature() != null &&
                tile.getContainedFeature() != null &&
                tile.getContainedFeature().getFeatureType() == FeatureType.SWAMP &&
                civilization.doesContainTechnology(TechnologyType.MASONRY) != 1)
            return false;
        if (improvementType == ImprovementType.FARM &&
                tile.getContainedFeature() != null &&
                tile.getContainedFeature().getFeatureType() == FeatureType.SWAMP &&
                civilization.doesContainTechnology(TechnologyType.MASONRY) != 1) return false;
        return improvementType != ImprovementType.FARM ||
                tile.getContainedFeature() == null ||
                tile.getContainedFeature().getFeatureType() != FeatureType.FOREST ||
                civilization.doesContainTechnology(TechnologyType.MINING) == 1;
    }

    public  boolean canHaveTheImprovement(Tile tile, ImprovementType improvementType,Unit unit) {
        if (tile.getCivilization() != unit.getCivilization())
            return false;
        return (tile.getContainedFeature() != null &&
                FeatureType.doesContainImprovement(tile.getContainedFeature().getFeatureType(),
                        improvementType)) ||
                TileType.canContainImprovement(tile.getTileType(), improvementType);
    }


    private  void setCivilizations(ArrayList<User> users) {
        civilizations = new ArrayList<>();
        for (int i = 0; i < users.size(); i++)
            civilizations.add(new Civilization(users.get(i), i));
    }


     boolean canUnitAttack(Tile tile,Unit unit) {
        if (tile.getCity() != null &&
                tile.getCity().getCivilization() != civilizations.get(playerTurn)) {
            return canUnitAttackCivResult(tile.getCity().getCivilization(),unit);
        }

        if (tile.getNonCivilian() != null &&
                tile.getNonCivilian().getCivilization() != civilizations.get(playerTurn)) {
            return canUnitAttackCivResult(tile.getNonCivilian().getCivilization(),unit);
        }
        if (tile.getCivilian() != null &&
                tile.getCivilian().getCivilization() != civilizations.get(playerTurn) &&
                unit.getUnitType().range > 1) {
            return canUnitAttackCivResult(tile.getCivilian().getCivilization(),unit);
        }
        return false;
    }

    private  boolean canUnitAttackCivResult(Civilization opponent,Unit unit)
    {
        if (this.getCurrentCivilization().knownCivilizationStatue(opponent)!=-1) {
            game.getSocketHandlers().get(game.getGameController().getPlayerTurn()).send(opponent.getUser().getNickname());
            String result = game.getSocketHandlers().get(game.getGameController().getPlayerTurn()).getScanner().nextLine();
            if (result.startsWith("ok")) {
                this.getCurrentCivilization().setKnownCivilizations(opponent,-1);
            } else{
                return false;
            }
        }
        unit.setState(UnitState.ATTACK);
        return true;
    }

    public  boolean nextTurnIfYouCan() {
        if (unfinishedTasks.size() != 0) return false;
        nextTurn();
        return true;
    }

    public  void shouldGameEnd() {
        Civilization winner = null;
        boolean returner = this.getCycle() >= 2050;
        if (!returner) {
            int numberOfCivilizationsLeft = 0;
            for (Civilization civilization : civilizations)
                if (civilization.isCivilizationAlive()) {
                    numberOfCivilizationsLeft++;
                    winner = civilization;
                    returner = true;
                    if (numberOfCivilizationsLeft > 1) {
                        winner = null;
                        returner = false;
                        break;
                    }
                }
            if (!returner) {
                int numberOfCapitalsLeft = 0;
                for (Civilization civilization : civilizations) {
                    if (civilization.getMainCapital() == null || civilization.getMainCapital().getCivilization() == civilization) {
                        numberOfCapitalsLeft++;
                        winner = civilization;
                        returner = true;
                        if (numberOfCapitalsLeft > 1) {
                            winner = null;
                            returner = false;
                            break;
                        }
                    }
                }
            }
        }
        if (returner)
            endTheGame(winner);
    }

    public  void endTheGame(Civilization winner) {
        if (winner == null) {
            int maxScore = -10000;
            for (Civilization civilization : civilizations) {
                int civilizationScore = civilization.getScore(this);
                if (civilization.getUser().getScore() < civilizationScore) {
                    civilization.getUser().setScore(civilizationScore);
//                    civilization.getUser().setHighestScoreDate(LocalDateTime.now());
                }
                if (civilizationScore > maxScore) {
                    winner = civilization;
                    maxScore = civilizationScore;
                }
            }
        }
        winnerSend = winner;
        game.end();
    }

    public  void nextTurn() {
        shouldGameEnd();

        civilizations.get(playerTurn).endTheTurn(this,game.getSocketHandlers().get(game.getGameController().getPlayerTurn()));
        playerTurn = (playerTurn + 1) % civilizations.size();
        if (civilizations.get(playerTurn).getCities().size() == 0 &&
                civilizations.get(playerTurn).getUnits().size() == 0) {
            boolean isItTheEnd = true;
            for (int i = 0; i < civilizations.size(); i++) {
                if (civilizations.get(playerTurn).getCities().size() != 0 ||
                        civilizations.get(playerTurn).getUnits().size() != 0) {
                    isItTheEnd = false;
                    break;
                }
            }
            if (!isItTheEnd) nextTurn();
            return;
        }
        if (playerTurn % civilizations.size() == 0)
            cycle++;
        civilizations.get(playerTurn).startTheTurn(this,game.getSocketHandlers().get(game.getGameController().getPlayerTurn()));
        setUnfinishedTasks();
        if (civilizations.get(playerTurn).getCities().size() != 0)
            game.getMapCommandsController().mapShowPosition(civilizations.get(playerTurn).getCities()
                            .get(0).getMainTile().getX() - getMap().WINDOW_X / 2,
                    civilizations.get(playerTurn).getCities().get(0)
                            .getMainTile().getY() - getMap().WINDOW_Y / 2 + 1);
        else if (civilizations.get(playerTurn).getUnits().size() != 0)
            game.getMapCommandsController().mapShowPosition(civilizations.get(playerTurn).getUnits().get(0)
                            .getCurrentTile().getX() - getMap().WINDOW_X / 2,
                    civilizations.get(playerTurn).getUnits().get(0)
                            .getCurrentTile().getY() - getMap().WINDOW_Y / 2 + 1);
        for (Unit unit : civilizations.get(playerTurn).getUnits()) {
            if (unit.getState() == UnitState.AWAKE) {
                break;
            }
        }
        if (this.getCivilizations()
                .get(this.getPlayerTurn()).getNotifications().containsKey(cycle))
            for (String string : this.getCivilizations()
                    .get(this.getPlayerTurn()).getNotifications().get(cycle)) {
                game.getSocketHandlers().get(game.getGameController().getPlayerTurn()).sendUpdate("notif",cycle + ";;"+ string);
            }
    }

    public  void setUnfinishedTasks() {
        unfinishedTasks = new ArrayList<>();
        for (int i = 0; i < civilizations.get(playerTurn).getUnits().size(); i++)
            if (civilizations.get(playerTurn).getUnits().get(i).getRemainedCost() == 0 &&
                    civilizations.get(playerTurn).getUnits().get(i).getState() == UnitState.AWAKE &&
                    civilizations.get(playerTurn).getUnits().get(i).getDestinationTile() == null)
                unfinishedTasks.add(new Tasks(civilizations.get(playerTurn).getUnits()
                        .get(i).getCurrentTile(), TaskTypes.UNIT));
        for (int i = 0; i < civilizations.get(playerTurn).getCities().size(); i++)
            if (civilizations.get(playerTurn).getCities().get(i).getProduct() == null)
                unfinishedTasks.add(new Tasks(civilizations.get(playerTurn)
                        .getCities().get(i).getMainTile(), TaskTypes.CITY_PRODUCTION));
        if (civilizations.get(playerTurn).getCities().size() != 0 &&
                civilizations.get(playerTurn).getGettingResearchedTechnology() == null &&
                !civilizations.get(playerTurn).areTechnologiesFinished())
            unfinishedTasks.add(new Tasks(null, TaskTypes.TECHNOLOGY_PROJECT));
    }

    private  boolean secondForOpenArea(int i, Tile tile, Civilization civilization,
                                             Unit unit, boolean isThereAnyEnemy) {
        for (int j = 0; j < 6; j++) {
            if (tile.getNeighbours(i).getNeighbours(j) == null)
                continue;
            checkForRuins(tile.getNeighbours(i).getNeighbours(j), civilization);
            checkForRuins(tile.getNeighbours(i).getNeighbours(j), civilization);
            int neighbourX = tile.getNeighbours(i).getNeighbours(j).getX();
            int neighbourY = tile.getNeighbours(i).getNeighbours(j).getY();
            civilization.getTileConditions()[neighbourX][neighbourY] =
                    new Civilization.TileCondition(tile.getNeighbours(i).getNeighbours(j)
                            .cloneTileForCivilization(civilization, game.getGameController(), unit), true);
            if (unit != null &&
                    ((tile.getNeighbours(i).getNeighbours(j).getCivilian() != null &&
                            tile.getNeighbours(i).getNeighbours(j)
                                    .getCivilian().getCivilization() != civilization) ||
                            (tile.getNeighbours(i).getNeighbours(j).getNonCivilian() != null &&
                                    tile.getNeighbours(i).getNeighbours(j)
                                            .getNonCivilian().getCivilization() != civilization))) {
                isThereAnyEnemy = true;
            }
        }
        return isThereAnyEnemy;
    }

    private  void checkForRuins(Tile tile, Civilization civilization) {
        if (tile.getRuins() != null && !tile.getRuins().getCivilizations().contains(civilization)) {
            game.getSocketHandlers().get(game.getGameController().getPlayerTurn()).sendUpdate("errorMaker","ruins found!" +";;"+ "there are some ruins around you " +
                    tile.getX() + ", " + tile.getY()+ ";;"+"i");
            tile.getRuins().getCivilizations().add(civilization);
        }
    }

    private  void checkForOtherCivilizations(Civilization civilization, Tile tile) {

        if (tile.getCity() != null &&
                tile.getCity().getCivilization() != civilization &&
                !civilization.knownCivilizationsContains(tile.getCity().getCivilization()))
            civilization.getKnownCivilizations().add(new AbstractMap.SimpleImmutableEntry<>(tile.getCity().getCivilization(), 0));
        if (tile.getCivilization() != null &&
                tile.getCivilization() != civilization &&
                !civilization.knownCivilizationsContains(tile.getCivilization()))
            civilization.getKnownCivilizations().add(new AbstractMap.SimpleImmutableEntry<>(tile.getCivilization(), 0));
        if (tile.getCivilian() != null &&
                tile.getCivilian().getCivilization() != civilization &&
                !civilization.knownCivilizationsContains(tile.getCivilian().getCivilization()))
            civilization.getKnownCivilizations().add(new AbstractMap.SimpleImmutableEntry<>(tile.getCivilian().getCivilization(), 0));
        if (tile.getNonCivilian() != null &&
                tile.getNonCivilian().getCivilization() != civilization &&
                !civilization.knownCivilizationsContains(tile.getNonCivilian().getCivilization()))
            civilization.getKnownCivilizations().add(new AbstractMap.SimpleImmutableEntry<>(tile.getNonCivilian().getCivilization(), 0));
    }

    public  boolean openNewArea(Tile tile, Civilization civilization, Unit unit) {
        boolean isThereAnyEnemy = false;
        for (int i = 0; i < 6; i++) {
            if (tile.getNeighbours(i) == null)
                continue;
            checkForOtherCivilizations(civilization, tile.getNeighbours(i));
            checkForRuins(tile.getNeighbours(i), civilization);
            civilization.getTileConditions()[tile.getNeighbours(i).getX()][tile.getNeighbours(i).getY()] =
                    new Civilization.TileCondition(tile.getNeighbours(i).
                            cloneTileForCivilization(civilization, game.getGameController(), unit), true);
            if (unit != null && ((tile.getNeighbours(i).getCivilian() != null &&
                    tile.getNeighbours(i).getCivilian().getCivilization() != civilization) ||
                    (tile.getNeighbours(i).getNonCivilian() != null &&
                            tile.getNeighbours(i).getNonCivilian()
                                    .getCivilization() != civilization))) {
                isThereAnyEnemy = true;
            }
            if (tile.getNeighbours(i).getTileType() == TileType.MOUNTAIN ||
                    tile.getNeighbours(i).getTileType() == TileType.HILL ||
                    (tile.getNeighbours(i).getContainedFeature() != null &&
                            tile.getNeighbours(i).getContainedFeature().getFeatureType() == FeatureType.JUNGLE))
                continue;
            isThereAnyEnemy = secondForOpenArea(i, tile, civilization, unit, isThereAnyEnemy);
        }
        civilization.getTileConditions()[tile.getX()][tile.getY()] =
                new Civilization.TileCondition(tile.
                        cloneTileForCivilization(civilization, game.getGameController(), unit), true);
        if (isThereAnyEnemy && unit != null && unit.getState() == UnitState.ALERT)
            unit.setState(UnitState.AWAKE);
        return isThereAnyEnemy;
    }

    public  Map getMap() {
        return map;
    }

    public  String printMap() {
        return playerTurn + 1 + ". " + civilizations.get(playerTurn).getUser().getNickname() + ":\n" +
                map.printMap(civilizations.get(playerTurn).getTileConditions(),
                        startWindowX, startWindowY);
    }



    private  void startProducingsOperation(UnitType tempType,City city) {
        for (Unit unit : city.getHalfProducedUnits())
            if (unit.getRemainedCost() != 0 && unit.getUnitType() == tempType) {
                city.setProduct(unit);
                this.deleteFromUnfinishedTasks(new Tasks(city.getMainTile(),
                        TaskTypes.CITY_PRODUCTION));
                return;
            }
        if (tempType.combatType == CombatType.CIVILIAN) {
            Civilian civilian = new Civilian(city.getMainTile(),
                    civilizations.get(playerTurn), tempType);
            city.getHalfProducedUnits().add(civilian);
            city.setProduct(civilian);
        } else {
            NonCivilian nonCivilian = new NonCivilian(city.getMainTile(),
                    civilizations.get(playerTurn), tempType);
            city.getHalfProducedUnits().add(nonCivilian);
            city.setProduct(nonCivilian);
        }
        if (tempType.getResourcesType() != null) {
            int temp = civilizations.get(playerTurn).getResourcesAmount().get(tempType.getResourcesType());
            civilizations.get(playerTurn).getResourcesAmount().remove(tempType.getResourcesType());
            if (temp != 1)
                civilizations.get(playerTurn).getResourcesAmount().put(tempType.getResourcesType(), temp - 1);
        }
        this.deleteFromUnfinishedTasks(new Tasks(city.getMainTile(),
                TaskTypes.CITY_PRODUCTION));
        civilizations.get(playerTurn).putNotification(city.getName() + ": " +
                tempType + "'s production started", cycle,game.getSocketHandlers().get(game.getGameController().getPlayerTurn()));
    }

    public  int startProducingUnit(String productIcon,City city) {
        UnitType tempType = UnitType.stringToEnum(productIcon);
//        if (tempType == null) return 1;
//        if (selectedCity == null) return 2;
//        if (selectedCity.getCivilization() != civilizations.get(playerTurn)) return 3;
        if (tempType.getResourcesType() != null &&
                (!civilizations.get(playerTurn).getResourcesAmount()
                        .containsKey(tempType.getResourcesType()) ||
                        (civilizations.get(playerTurn).getResourcesAmount()
                                .containsKey(tempType.getResourcesType()) &&
                                civilizations.get(playerTurn)
                                        .getResourcesAmount().get(tempType.getResourcesType()) == 0)))
            return 5;
//        if (civilizations.get(playerTurn).doesContainTechnology(tempType.getTechnologyRequired()) != 1)
//            return 6;
        startProducingsOperation(tempType,city);
        return 0;
    }

    public  ArrayList<Civilization> getCivilizations() {
        return civilizations;
    }


    public  int getPlayerTurn() {
        return playerTurn;
    }

    public  ArrayList<Tasks> getUnfinishedTasks() {
        return unfinishedTasks;
    }


    public  void setMap(Map map) {
        this.map = map;
    }



    public  int getCycle() {
        return cycle;
    }



    public  void setCivilizationsAsList(ArrayList<Civilization> civilizations) {
        this.civilizations = civilizations;
    }

    public  void setUnfinishedTasks(ArrayList<Tasks> unfinishedTasks) {
        this.unfinishedTasks = unfinishedTasks;
    }

    public  void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public  Civilization getWinner() {
        return winnerSend;
    }


    public  Civilization getCurrentCivilization() {
        return civilizations.get(playerTurn);
    }
}
