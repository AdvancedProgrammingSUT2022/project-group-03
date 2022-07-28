package com.example.demo.view;

import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.City;
import com.example.demo.model.Civilization;
import com.example.demo.model.TradeRequest;
import com.example.demo.model.Units.Civilian;
import com.example.demo.model.Units.NonCivilian;
import com.example.demo.model.resources.ResourcesTypes;
import com.google.gson.Gson;
import com.example.demo.model.Units.Unit;
import com.example.demo.model.Units.UnitType;
import com.example.demo.model.building.BuildingType;
import com.example.demo.model.improvements.ImprovementType;
import com.example.demo.model.technologies.Technology;
import com.example.demo.model.technologies.TechnologyType;
import com.example.demo.network.GameHandler;
import com.example.demo.network.MySocketHandler;

import java.util.AbstractMap;
import java.util.Map;
import java.util.regex.Matcher;

public class GameView extends Menu{
    public boolean turn = false;
    {
        regexes = new String[]{
                "^menu exit$",
                "^cheat re$",
                "^cheat om$",
                "^cheat te$",
                "^create unit \\w+",
                "^increase gold \\d+",
                "^reassign (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+)",
                "^buyTile (\\d+) (\\d+) (\\d+) (\\d+)",
                "^cityAttack (\\d+) (\\d+) (\\d+) (\\d+)",
                "^assign (\\d+) (\\d+) (\\d+) (\\d+)",
                "^buyUnit (\\w+) (\\d+) (\\d+)",
                "^startProductionUnit (\\w+) (\\d+) (\\d+)",
                "^removeCitizen (\\d+) (\\d+) (\\d+) (\\d+)",//12
                "^buildBuilding (\\d+) (\\d+) (\\d+) (\\d+) (\\w+) (.+)",
                "^cityDestiny (\\d+) (\\d+) (\\w+)",
                "^nextTurn",
                "^tech (\\S+)",
                "^unitAttack (\\d+) (\\d+) (\\d+ \\d+ \\w+)",
                "^moveto (\\d+) (\\d+) (\\d+ \\d+ \\w+)",
                "^sleep (\\d+ \\d+ \\w+)",//19
                "^state (\\d+ \\d+ \\w+) (\\d+)",
                "^upgrade (\\d+ \\d+ \\w+)",
                "^alert (\\d+ \\d+ \\w+)",
                "^setup (\\d+ \\d+ \\w+)",
                "^foundCity (\\d+ \\d+ \\w+)",//24
                "^cancel (\\d+ \\d+ \\w+)",
                "^delete (\\d+ \\d+ \\w+)",
                "^unitBuild (\\d+ \\d+ \\w+) (\\S+)",
                "^unitBuildRoad (\\d+ \\d+ \\w+)",
                "^unitBuildRailRoad (\\d+ \\d+ \\w+)",
                "^unitRemoveFromTile (\\d+ \\d+ \\w+) (\\S+)",//30
                "^unitRepair (\\d+ \\d+ \\w+)",
                "^pillage (\\d+ \\d+ \\w+)",
                "^tradeRequest (.+);;(.+)",
                "^update", //34
                "peaceRequest (.+)", //35
                "acceptTrade (\\d+) (.+)", //36
                "declineTrade (\\d+)", //37
                "acceptPeace (\\d+)", //38
                "declinePeace (\\d+)", //39

        };
    }
    private GameHandler game ;
    public GameView(MySocketHandler socketHandler) {
        super(socketHandler);
    }
    public void setGame(GameHandler game){
        this.game = game;
    }

    @Override
    protected boolean commands(String command)
    {
        if(!turn && !command.equals("update")){
            socketHandler.send("not your turn");
            return false;
        }
        commandNumber = getCommandNumber(command, regexes,false);
        switch (commandNumber) {
            case -1:
                System.out.println("invalid command");
                socketHandler.send("9");
                break;
            case 0:
                socketHandler.send("exited successfully");
                nextMenu = 1;
                return true;
            case 1:
                game.getCheatCommandsController().cheatRoadEverywhere();
                socketHandler.send("");
                break;
            case 2:
                game.getCheatCommandsController().openMap();
                socketHandler.send("");
                break;
            case 3:
                for (TechnologyType technologyType : TechnologyType.values())
                    game.getCheatCommandsController().cheatTechnology(technologyType);
                socketHandler.send("");
                break;
            case 4:
                UnitType unitType = UnitType.stringToEnum(command.substring(13));
                game.getCheatCommandsController().cheatUnit(10, 10, unitType);
                socketHandler.send("");
                break;
            case 5:
                int gold = Integer.parseInt(command.substring(14));
                game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).increaseGold(gold);
                socketHandler.send("");
                break;
            case 6:
                Matcher matcher = getMatcher(regexes[6],command,false);
                socketHandler.send(String.valueOf(game.getCityCommandsController().reAssignCitizen(Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)),Integer.parseInt(matcher.group(4)),
                        game.getMap().coordinatesToTile(Integer.parseInt(matcher.group(5)),Integer.parseInt(matcher.group(6))).getCity())));
                break;
            case 7:
                matcher = getMatcher(regexes[7],command,false);
                socketHandler.send(String.valueOf(game.getCityCommandsController().buyTile(Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)),
                        game.getMap().coordinatesToTile(Integer.parseInt(matcher.group(3)),Integer.parseInt(matcher.group(4))).getCity())));
                break;
            case 8:
                matcher = getMatcher(regexes[8],command,false);
                socketHandler.send(String.valueOf(game.getCityCommandsController().cityAttack(Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)),
                        game.getMap().coordinatesToTile(Integer.parseInt(matcher.group(3)),Integer.parseInt(matcher.group(4))).getCity())));
                break;
            case 9:
                matcher = getMatcher(regexes[9],command,false);
                socketHandler.send(String.valueOf(game.getCityCommandsController().assignCitizen(Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)),
                        game.getMap().coordinatesToTile(Integer.parseInt(matcher.group(3)),Integer.parseInt(matcher.group(4))).getCity())));
                break;
            case 10:
                matcher = getMatcher(regexes[10],command,false);
                socketHandler.send(String.valueOf(game.getCityCommandsController().buyUnit(matcher.group(1),
                        Integer.parseInt(matcher.group(2)),
                        Integer.parseInt(matcher.group(3)))));
                break;
            case 11:
                matcher = getMatcher(regexes[11],command,false);
                socketHandler.send(String.valueOf(game.getGameController().startProducingUnit(matcher.group(1),
                        game.getMap().coordinatesToTile(Integer.parseInt(matcher.group(2)),Integer.parseInt(matcher.group(3))).getCity())));
                break;
            case 12:
                matcher = getMatcher(regexes[12],command,false);
                socketHandler.send(String.valueOf(game.getCityCommandsController().removeCitizen(Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)),
                        game.getMap().coordinatesToTile(Integer.parseInt(matcher.group(2)),Integer.parseInt(matcher.group(3))).getCity())));
                break;
            case 13:
                matcher = getMatcher(regexes[13],command,false);
                socketHandler.send(String.valueOf(game.getCityCommandsController()
                        .buildBuilding((BuildingType) new Gson().fromJson(matcher.group(6), BuildingType.class),
                        game.getMap().coordinatesToTile(Integer.parseInt(matcher.group(1)),Integer.parseInt(matcher.group(2))),
                                game.getMap().coordinatesToTile(Integer.parseInt(matcher.group(3)),Integer.parseInt(matcher.group(4))).getCity(),
                                Boolean.parseBoolean(matcher.group(5)))));
                break;
            case 14:
                matcher = getMatcher(regexes[14],command,false);
                socketHandler.send(String.valueOf(game.getCityCommandsController()
                        .cityDestiny(Boolean.parseBoolean(matcher.group(2)),
                                game.getMap().coordinatesToTile(Integer.parseInt(matcher.group(1)),Integer.parseInt(matcher.group(2))).getCity())));

                break;
            case 15:
                socketHandler.send(String.valueOf(game.getGameController().nextTurnIfYouCan()));
                break;
            case 16:
                matcher = getMatcher(regexes[16],command,false);
                socketHandler.send(String.valueOf(game.getTechnologyAndProductionController().addTechnologyToProduction(new Gson().fromJson(matcher.group(1), Technology.class))));
                SavingHandler.save(socketHandler);
                break;
            case 17:
                matcher = getMatcher(regexes[17],command,false);
                Unit unit = stringToUnit(matcher.group(3));
                socketHandler.send(String.valueOf(game.getUnitStateController().unitAttack(Integer.parseInt(matcher.group(1)),Integer.parseInt(matcher.group(2)),unit)));
                break;
            case 18:
                matcher = getMatcher(regexes[18],command,false);
                unit = stringToUnit(matcher.group(3));
                socketHandler.send(String.valueOf(game.getUnitStateController().unitMoveTo(Integer.parseInt(matcher.group(1)),Integer.parseInt(matcher.group(2)),unit)));
                break;
            case 19:
                matcher = getMatcher(regexes[19],command,false);
                unit =stringToUnit(matcher.group(1));
                game.getUnitStateController().unitSleep(unit);
                socketHandler.send("done");
                break;
            case 20:
                matcher = getMatcher(regexes[20],command,false);
                unit = stringToUnit(matcher.group(1));
                socketHandler.send(String.valueOf(game.getUnitStateController().unitChangeState(Integer.parseInt(matcher.group(2)),unit)));
                break;
            case 21:
                matcher = getMatcher(regexes[21],command,false);
                unit = stringToUnit(matcher.group(1));
                game.getUnitStateController().unitUpgrade(unit);
                socketHandler.send("done");
                break;
            case 22:
                matcher = getMatcher(regexes[22],command,false);
                unit = stringToUnit(matcher.group(1));
                socketHandler.send(String.valueOf(game.getUnitStateController().unitAlert(unit)));
                break;
            case 23:
                matcher = getMatcher(regexes[23],command,false);
                unit = stringToUnit(matcher.group(1));
                game.getUnitStateController().unitSetupRanged(unit);
                socketHandler.send("done");
                break;
            case 24:
                matcher = getMatcher(regexes[24],command,false);
                unit = stringToUnit(matcher.group(1));
                socketHandler.send(String.valueOf(game.getUnitStateController().unitFoundCity("unnamed",unit)));
                break;
            case 25:
                matcher = getMatcher(regexes[25],command,false);
                unit = stringToUnit(matcher.group(1));
                socketHandler.send(String.valueOf(game.getUnitStateController().unitCancelMission(unit)));
                break;
            case 26:
                matcher = getMatcher(regexes[26],command,false);
                unit = stringToUnit(matcher.group(1));
                game.getUnitStateController().unitDelete(unit);
                socketHandler.send("done");
                break;
            case 27:
                matcher = getMatcher(regexes[27],command,false);
                unit = stringToUnit(matcher.group(1));
                ImprovementType improvementType = new Gson().fromJson(matcher.group(2),ImprovementType.class);
                game.getUnitStateController().unitBuild(improvementType,unit);
                socketHandler.send("done");
                break;
            case 28:
                matcher = getMatcher(regexes[28],command,false);
                unit =stringToUnit(matcher.group(1));
                game.getUnitStateController().unitBuildRoad(unit);
                socketHandler.send("done");
                break;
            case 29:
                matcher = getMatcher(regexes[29],command,false);
                unit = stringToUnit(matcher.group(1));
                game.getUnitStateController().unitBuildRailRoad(unit);
                socketHandler.send("done");
                break;
            case 30:
                matcher = getMatcher(regexes[30],command,false);
                unit = stringToUnit(matcher.group(1));
                game.getUnitStateController().unitRemoveFromTile(Boolean.parseBoolean(matcher.group(2)),unit);
                socketHandler.send("done");
                break;
            case 31:
                matcher = getMatcher(regexes[31],command,false);
                unit =stringToUnit(matcher.group(1));
                game.getUnitStateController().unitRepair(unit);
                socketHandler.send("done");
                break;
            case 32:
                matcher = getMatcher(regexes[32],command,false);
                unit = stringToUnit(matcher.group(1));
                socketHandler.send(String.valueOf(game.getUnitStateController().unitPillage(unit)));
                break;
            case 33:
                matcher = getMatcher(regexes[33],command,false);
                Civilization civilization = null;
                for (Civilization civilization1 : game.getGameController().getCivilizations()) {
                    if(civilization1.getUser().getUsername().equals(matcher.group(1))){
                        civilization = civilization1;
                    }
                }
                TradeRequest tradeRequest = new Gson().fromJson(matcher.group(2),TradeRequest.class);
                if(civilization != null) civilization.getTradeRequests().add(tradeRequest);
                socketHandler.send("done");
                break;
            case 34:
                if(socketHandler.getGame().over){
                    socketHandler.send("end");
                    SavingHandler.save(socketHandler);
                    socketHandler.send(game.getGameController().getWinner().getUser().getUsername());
                    nextMenu = 1;
                    socketHandler.setGame(null);
                    return true;
                }else {
                    if(turn){
                        socketHandler.send("your turn");
                        SavingHandler.save(socketHandler);

                    }
                    else {
                        socketHandler.send("sdfasdfadf");
                        SavingHandler.save(socketHandler);
                    }

                }
                break;
            case 35:
                matcher = getMatcher(regexes[35],command,false);
                civilization = null;
                for (Civilization civilization1 : game.getGameController().getCivilizations()) {
                    if(civilization1.getUser().getUsername().equals(matcher.group(1))){
                        civilization = civilization1;
                    }
                }
                if(civilization != null) civilization.getFriendshipRequests().add(game.getGameController().getCurrentCivilization());
                socketHandler.send("done");
                break;
            case 36:
                matcher = getMatcher(regexes[36],command,false);
                civilization = null;
                for (Civilization civilization1 : game.getGameController().getCivilizations()) {
                    if(civilization1.getUser().getUsername().equals(matcher.group(2))){
                        civilization = civilization1;
                    }
                }
                case36(Integer.parseInt(matcher.group(1)),civilization);
                socketHandler.send("done");
                break;
            case 37:
                matcher = getMatcher(regexes[37],command,false);
                game.getGameController().getCurrentCivilization().getTradeRequests().remove(Integer.parseInt(matcher.group(1)));
                socketHandler.send("done");
                break;
            case 38:
                matcher = getMatcher(regexes[38],command,false);
                game.getGameController().getCurrentCivilization().getTradeRequests().remove(Integer.parseInt(matcher.group(1)));
                socketHandler.send("done");
                break;
            case 39:
                matcher = getMatcher(regexes[39],command,false);
                game.getGameController().getCurrentCivilization().getFriendshipRequests().remove(Integer.parseInt(matcher.group(1)));
                socketHandler.send("done");
                break;


        }
        return false;
    }
    private Unit stringToUnit(String string){
        Matcher matcher = getMatcher("(\\d+) (\\d+) (\\w+)",string,false);
        if(matcher.group(3).equals("c")){
            return game.getGameController().getMap().coordinatesToTile(Integer.parseInt(matcher.group(1)),Integer.parseInt(matcher.group(2))).getCivilian();
        }
        else {
            return game.getGameController().getMap().coordinatesToTile(Integer.parseInt(matcher.group(1)),Integer.parseInt(matcher.group(2))).getNonCivilian();
        }
    }
    private void case36(int finalI,Civilization opponent){
        TradeRequest tradeRequest = game.getGameController().getCurrentCivilization().getTradeRequests().get(finalI);
        for (int i1 = tradeRequest.getTheirOffers().size() - 1; i1 >= 0; i1--) {
            game.getGameController().getCurrentCivilization().addResources(tradeRequest.getTheirOffers().get(i1).getKey(),
                    tradeRequest.getTheirOffers().get(i1).getValue());
            tradeRequest.getFrom().removeResource(tradeRequest.getTheirOffers().get(i1).getKey(),
                    tradeRequest.getTheirOffers().get(i1).getValue());
        }
        for (int i1 = tradeRequest.getYourOffers().size() - 1; i1 >= 0; i1--) {
            tradeRequest.getFrom().addResources(tradeRequest.getTheirOffers().get(i1).getKey(),
                    tradeRequest.getTheirOffers().get(i1).getValue());
            game.getGameController().getCurrentCivilization().removeResource(tradeRequest.getTheirOffers().get(i1).getKey(),
                    tradeRequest.getTheirOffers().get(i1).getValue());
        }
        game.getGameController().getCurrentCivilization().increaseGold(tradeRequest.theirGoldOffer() - tradeRequest.yourGoldOffer());
        opponent.increaseGold(tradeRequest.yourGoldOffer() - tradeRequest.theirGoldOffer());
        game.getGameController().getCurrentCivilization().getTradeRequests().remove(finalI);
    }
    private void acceptPeace(int finalI){
        if (game.getGameController().getCurrentCivilization().knownCivilizationsContains(game.getGameController().getCurrentCivilization().getFriendshipRequests().get(finalI))) {
            for (Map.Entry<Civilization, Integer> knownCivilization : game.getGameController().getCurrentCivilization().getKnownCivilizations()) {
                if (knownCivilization.getKey() == game.getGameController().getCurrentCivilization().getFriendshipRequests().get(finalI)) {
                    game.getGameController().getCurrentCivilization().getKnownCivilizations().remove(knownCivilization);
                    break;
                }
            }
        }
        game.getGameController().getCurrentCivilization().getKnownCivilizations()
                .add(new AbstractMap.SimpleImmutableEntry<>(game.getGameController().getCurrentCivilization().getFriendshipRequests().get(finalI), 1));
        game.getGameController().getCurrentCivilization().getFriendshipRequests().remove(finalI);
    }
}
