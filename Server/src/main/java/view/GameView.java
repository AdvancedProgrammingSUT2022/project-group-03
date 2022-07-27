package view;

import com.google.gson.Gson;
import controller.gameController.CheatCommandsController;
import controller.gameController.UnitStateController;
import model.Units.NonCivilian;
import model.Units.Unit;
import model.Units.UnitType;
import model.building.BuildingType;
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
        commandNumber = getCommandNumber(command, regexes,true);
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
                break;



        }
        return false;
    }
}
