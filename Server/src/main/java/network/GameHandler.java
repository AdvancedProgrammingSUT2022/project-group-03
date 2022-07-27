package network;

import com.beust.ah.A;
import controller.TechnologyAndProductionController;
import controller.gameController.*;
import model.Map;
import model.User;

import java.util.ArrayList;

public class GameHandler {
    private static ArrayList<GameHandler> list = new ArrayList<>();
    private GameController gameController;
    private CheatCommandsController cheatCommandsController;
    private CityCommandsController cityCommandsController;
    private MapCommandsController mapCommandsController;
    private TileXAndYFlagSelectUnitController tileXAndYFlagSelectUnitController;
    private UnitStateController unitStateController;
    private TechnologyAndProductionController technologyAndProductionController;
    private ArrayList<MySocketHandler> socketHandlers;
    public boolean over = false;
    public boolean started;
    private Map map;
    public static GameHandler findGame(User user){
        for (GameHandler gameHandler : list) {
            if (gameHandler.socketHandlers.get(0).getLoginController().getLoggedUser() == user)
                return gameHandler;
        }
        return null;
    }
    public GameHandler(MySocketHandler socketHandler){
        socketHandlers = new ArrayList<>();
        socketHandlers.add(socketHandler);
        list.add(this);
    }


    public ArrayList<MySocketHandler> getSocketHandlers() {
        return socketHandlers;
    }

    public CheatCommandsController getCheatCommandsController() {
        return cheatCommandsController;
    }

    public CityCommandsController getCityCommandsController() {
        return cityCommandsController;
    }

    public GameController getGameController() {
        return gameController;
    }

    public MapCommandsController getMapCommandsController() {
        return mapCommandsController;
    }

    public TileXAndYFlagSelectUnitController getTileXAndYFlagSelectUnitController() {
        return tileXAndYFlagSelectUnitController;
    }

    public UnitStateController getUnitStateController() {
        return unitStateController;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return map;
    }
    public void startGame(int x,int y){

        ArrayList<User > list = getUsers();
        started = true;
        gameController = new GameController(this);
        cheatCommandsController = new CheatCommandsController(this);
        cityCommandsController = new CityCommandsController(this);
        mapCommandsController = new MapCommandsController(this);
        tileXAndYFlagSelectUnitController = new TileXAndYFlagSelectUnitController(this);
        unitStateController = new UnitStateController(this);
        technologyAndProductionController = new TechnologyAndProductionController(this);
        gameController.startGame(list,x,y);
    }
    public void end(){
        over = true;
    }
    public ArrayList<User> getUsers(){
        ArrayList<User> list = new ArrayList<>();
        for (MySocketHandler handler : socketHandlers) {
            list.add(handler.getLoginController().getLoggedUser());
        }
        return list;
    }
    public TechnologyAndProductionController getTechnologyAndProductionController() {
        return technologyAndProductionController;
    }
}
