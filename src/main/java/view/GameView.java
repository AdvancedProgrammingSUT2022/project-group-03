package view;

import network.GameHandler;
import network.MySocketHandler;

public class GameView extends Menu{
    {
        regexes = new String[]{
                "^menu exit$",
                "^menu show-current$",
                "^save (.*)",
                "^menu enter.*"
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
                System.out.println("Profile Menu");
                break;
            case 2: {
                //changeNickname(command);
            } break;
            case 3:
                System.out.println("menu navigation is not possible");
                break;
        }
        return false;
    }
}
