package view;

import com.google.gson.Gson;
import controller.gameController.CheatCommandsController;
import model.Units.UnitType;
import model.building.BuildingType;
import model.technologies.Technology;
import model.technologies.TechnologyType;
import network.GameHandler;
import network.MySocketHandler;

import java.util.regex.Matcher;

public class GameView extends Menu{
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
                "^startProductionUnit (\\w+)",
                "^removeCitizen (\\d+) (\\d+) (\\d+)",//12
                "^buildBuilding (\\d+) (\\d+) (\\d+) (\\w+) (.+)",
                "^cityDestiny (\\d+) (\\w+)",
                "^nextTurn",
                "^tech (\\S+)"
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
        commandNumber = getCommandNumber(command, regexes,true);
        switch (commandNumber) {
            case -1:
                System.out.println("invalid command");
                break;
            case 0:
                socketHandler.send("exited successfully");
                nextMenu = 1;
                return true;
            case 1:
                game.getCheatCommandsController().cheatRoadEverywhere();
                break;
            case 2:
                game.getCheatCommandsController().openMap();
                break;
            case 3:
                for (TechnologyType technologyType : TechnologyType.values())
                    game.getCheatCommandsController().cheatTechnology(technologyType);
                break;
            case 4:
                UnitType unitType = UnitType.stringToEnum(command.substring(13));
                game.getCheatCommandsController().cheatUnit(10, 10, unitType);
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
                socketHandler.send(String.valueOf(game.getGameController().startProducingUnit(matcher.group(1))));
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
                game.getTechnologyAndProductionController().addTechnologyToProduction(new Gson().fromJson(matcher.group(1), Technology.class));
                break;


        }
        return false;
    }
}
