package view;

import com.google.gson.Gson;
import controller.gameController.CheatCommandsController;
import controller.gameController.UnitStateController;
import model.Units.NonCivilian;
import model.Units.Unit;
import model.Units.UnitType;
import model.building.BuildingType;
import model.improvements.Improvement;
import model.improvements.ImprovementType;
import model.technologies.Technology;
import model.technologies.TechnologyType;
import network.GameHandler;
import network.MySocketHandler;

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
                "^reassign (\\d+) (\\d+) (\\d+) (\\d+) (\\d+)",
                "^buyTile (\\d+) (\\d+) (\\d+)",
                "^cityAttack (\\d+) (\\d+) (\\d+)",
                "^assign (\\d+) (\\d+) (\\d+)",
                "^buyUnit (\\w+) (\\d+) (\\d+)",
                "^startProductionUnit (\\w+) (\\d+)",
                "^removeCitizen (\\d+) (\\d+) (\\d+)",//12
                "^buildBuilding (\\d+) (\\d+) (\\d+) (\\w+) (.+)",
                "^cityDestiny (\\d+) (\\w+)",
                "^nextTurn",
                "^tech (\\S+)",
                "^unitAttack (\\d+) (\\d+) (\\d+)",
                "^moveto (\\d+) (\\d+) (\\d+)",
                "^sleep (\\d+)",//19
                "^state (\\d+) (\\d+)",
                "^upgrade (\\d+)",
                "^alert (\\d+)",
                "^setup (\\d+)",
                "^foundCity (\\d+)",//24
                "^cancel (\\d+)",
                "^delete (\\d+)",
                "^unitBuild (\\d+) (\\S+)",
                "^unitBuildRoad (\\d+)",
                "^unitBuildRail (\\d+)",
                "^unitRemoveFromTile (\\d+) (\\S+)",//30
                "^unitRepair (\\d+)",
                "^pillage (\\d+)",
                "^update ", //33

        };
    }
    private GameHandler game ;
    public GameView(MySocketHandler socketHandler) {
        super(socketHandler);
        game = socketHandler.getGame();
    }

    @Override
    protected boolean commands(String command)
    {
        if(!turn){
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
                break;
            case 6:
                Matcher matcher = getMatcher(regexes[6],command,false);
                socketHandler.send(String.valueOf(game.getCityCommandsController().reAssignCitizen(Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)),Integer.parseInt(matcher.group(4)),
                        game.getGameController().getCivilizations().get(game.getGameController()
                                .getPlayerTurn()).getCities().get(Integer.parseInt(matcher.group(5))))));
                break;
            case 7:
                matcher = getMatcher(regexes[7],command,false);
                socketHandler.send(String.valueOf(game.getCityCommandsController().buyTile(Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)),
                        game.getGameController().getCivilizations()
                                .get(game.getGameController().getPlayerTurn()).getCities()
                                .get(Integer.parseInt(matcher.group(3))))));
                break;
            case 8:
                matcher = getMatcher(regexes[8],command,false);
                socketHandler.send(String.valueOf(game.getCityCommandsController().cityAttack(Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)),
                        game.getGameController().getCivilizations()
                                .get(game.getGameController().getPlayerTurn())
                                .getCities().get(Integer.parseInt(matcher.group(3))))));
                break;
            case 9:
                matcher = getMatcher(regexes[9],command,false);
                socketHandler.send(String.valueOf(game.getCityCommandsController().assignCitizen(Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)),
                        game.getGameController().getCivilizations()
                                .get(game.getGameController().getPlayerTurn())
                                .getCities().get(Integer.parseInt(matcher.group(3))))));
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
                        game.getGameController().getCivilizations().get(game.getGameController()
                                .getPlayerTurn()).getCities().get(Integer.parseInt(matcher.group(2))))));
                break;
            case 12:
                matcher = getMatcher(regexes[12],command,false);
                socketHandler.send(String.valueOf(game.getCityCommandsController().removeCitizen(Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)),
                        game.getGameController().getCivilizations()
                                .get(game.getGameController().getPlayerTurn())
                                .getCities().get(Integer.parseInt(matcher.group(3))))));
                break;
            case 13:
                matcher = getMatcher(regexes[13],command,false);
                socketHandler.send(String.valueOf(game.getCityCommandsController()
                        .buildBuilding((BuildingType) new Gson().fromJson(matcher.group(5), BuildingType.class),
                        game.getMap().coordinatesToTile(Integer.parseInt(matcher.group(1)),Integer.parseInt(matcher.group(2))),
                        game.getGameController().getCivilizations()
                                .get(game.getGameController().getPlayerTurn())
                                .getCities().get(Integer.parseInt(matcher.group(3))),
                                Boolean.parseBoolean(matcher.group(4)))));
                break;
            case 14:
                matcher = getMatcher(regexes[14],command,false);
                socketHandler.send(String.valueOf(game.getCityCommandsController()
                        .cityDestiny(Boolean.parseBoolean(matcher.group(2)), game.getGameController().getCivilizations()
                                .get(game.getGameController().getPlayerTurn())
                                .getCities().get(Integer.parseInt(matcher.group(1))))));

                break;
            case 15:
                socketHandler.send(String.valueOf(game.getGameController().nextTurnIfYouCan()));
                break;
            case 16:
                matcher = getMatcher(regexes[16],command,false);
                socketHandler.send(String.valueOf(game.getTechnologyAndProductionController().addTechnologyToProduction(new Gson().fromJson(matcher.group(1), Technology.class))));
                break;
            case 17:
                matcher = getMatcher(regexes[17],command,false);
                Unit unit = game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getUnits().get(Integer.parseInt(matcher.group(3)));
                socketHandler.send(String.valueOf(game.getUnitStateController().unitAttack(Integer.parseInt(matcher.group(1)),Integer.parseInt(matcher.group(2)),unit)));
                break;
            case 18:
                matcher = getMatcher(regexes[18],command,false);
                unit = game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getUnits().get(Integer.parseInt(matcher.group(3)));
                socketHandler.send(String.valueOf(game.getUnitStateController().unitMoveTo(Integer.parseInt(matcher.group(1)),Integer.parseInt(matcher.group(2)),unit)));
                break;
            case 19:
                matcher = getMatcher(regexes[19],command,false);
                unit = game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getUnits().get(Integer.parseInt(matcher.group(1)));
                game.getUnitStateController().unitSleep(unit);
                socketHandler.send("done");
                break;
            case 20:
                matcher = getMatcher(regexes[20],command,false);
                unit = game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getUnits().get(Integer.parseInt(matcher.group(1)));
                socketHandler.send(String.valueOf(game.getUnitStateController().unitChangeState(Integer.parseInt(matcher.group(2)),unit)));
                break;
            case 21:
                matcher = getMatcher(regexes[21],command,false);
                unit = game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getUnits().get(Integer.parseInt(matcher.group(1)));
                game.getUnitStateController().unitUpgrade(unit);
                socketHandler.send("done");
                break;
            case 22:
                matcher = getMatcher(regexes[22],command,false);
                unit = game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getUnits().get(Integer.parseInt(matcher.group(1)));
                socketHandler.send(String.valueOf(game.getUnitStateController().unitAlert(unit)));
                break;
            case 23:
                matcher = getMatcher(regexes[23],command,false);
                unit = game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getUnits().get(Integer.parseInt(matcher.group(1)));
                game.getUnitStateController().unitSetupRanged(unit);
                socketHandler.send("done");
                break;
            case 24:
                matcher = getMatcher(regexes[24],command,false);
                unit = game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getUnits().get(Integer.parseInt(matcher.group(1)));
                socketHandler.send(String.valueOf(game.getUnitStateController().unitFoundCity("unnamed",unit)));
                break;
            case 25:
                matcher = getMatcher(regexes[25],command,false);
                unit = game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getUnits().get(Integer.parseInt(matcher.group(1)));
                socketHandler.send(String.valueOf(game.getUnitStateController().unitCancelMission(unit)));
                break;
            case 26:
                matcher = getMatcher(regexes[26],command,false);
                unit = game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getUnits().get(Integer.parseInt(matcher.group(1)));
                game.getUnitStateController().unitDelete(unit);
                socketHandler.send("done");
                break;
            case 27:
                matcher = getMatcher(regexes[27],command,false);
                unit = game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getUnits().get(Integer.parseInt(matcher.group(1)));
                ImprovementType improvementType = new Gson().fromJson(matcher.group(2),ImprovementType.class);
                game.getUnitStateController().unitBuild(improvementType,unit);
                socketHandler.send("done");
                break;
            case 28:
                matcher = getMatcher(regexes[28],command,false);
                unit = game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getUnits().get(Integer.parseInt(matcher.group(1)));
                game.getUnitStateController().unitBuildRoad(unit);
                socketHandler.send("done");
                break;
            case 29:
                matcher = getMatcher(regexes[29],command,false);
                unit = game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getUnits().get(Integer.parseInt(matcher.group(1)));
                game.getUnitStateController().unitBuildRailRoad(unit);
                socketHandler.send("done");
                break;
            case 30:
                matcher = getMatcher(regexes[30],command,false);
                unit = game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getUnits().get(Integer.parseInt(matcher.group(1)));
                game.getUnitStateController().unitRemoveFromTile(Boolean.parseBoolean(matcher.group(2)),unit);
                socketHandler.send("done");
                break;
            case 31:
                matcher = getMatcher(regexes[31],command,false);
                unit = game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getUnits().get(Integer.parseInt(matcher.group(1)));
                game.getUnitStateController().unitRepair(unit);
                socketHandler.send("done");
                break;
            case 32:
                matcher = getMatcher(regexes[32],command,false);
                unit = game.getGameController().getCivilizations().get(game.getGameController().getPlayerTurn()).getUnits().get(Integer.parseInt(matcher.group(1)));
                socketHandler.send(String.valueOf(game.getUnitStateController().unitPillage(unit)));
                break;
            case 33:
                if(socketHandler.getGame().over){
                    socketHandler.send("end");
                }else {
                    if(turn){
                        socketHandler.send("your turn");
                    }
                    SavingHandler.save(socketHandler);
                }





        }
        return false;
    }
}
