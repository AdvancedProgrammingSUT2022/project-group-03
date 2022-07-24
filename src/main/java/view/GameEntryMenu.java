package view;

import com.google.gson.Gson;
import model.User;
import network.GameHandler;
import network.MySocketHandler;

import java.util.regex.Matcher;

public class GameEntryMenu extends Menu{
    {
        regexes = new String[]{
                "^menu exit$",
                "^get invite .*",
                "^create game .*",
                "^send invite (.+);",
                "^accept invite (.+);",
                "^start (\\d+) (\\d+);"
        };
    }
    private final String[] fieldRegexes = {
            ".*--current (\\S+).*",
            ".*--new (\\S+).*"
    };


    public GameEntryMenu(MySocketHandler socketHandler) {
        super(socketHandler);
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
                socketHandler.send("s");
                getInvites();
                break;
            case 2: {
                socketHandler.send("s");
                createGame();
            } break;
            case 3:
                socketHandler.send(String.valueOf(sendInvite(command)));
                break;
            case 4:
                socketHandler.send(String.valueOf(acceptInvite(command)));
                break;
            case 5:
                start(command);
                socketHandler.send("s");
                break;
        }
        return false;
    }
    private void getInvites()
    {
        socketHandler.send(new Gson().toJson(socketHandler.getLoginController().getLoggedUser()));
    }
    private void createGame()
    {
        socketHandler.setGame(new GameHandler(socketHandler));
    }
    private int sendInvite(String command)
    {
        Matcher matcher = getMatcher(regexes[3],command,false);
        User user = User.findUser(matcher.group(1),false);
        if(user == null) return 1;
        if(user.getInvites().contains(socketHandler.getLoginController().getLoggedUser())) return 2;
        user.getInvites().add(socketHandler.getLoginController().getLoggedUser());
        return 0;
    }
    private int acceptInvite(String command){
        Matcher matcher = getMatcher(regexes[4],command,false);
        User user = User.findUser(matcher.group(1),false);
        GameHandler game = GameHandler.findGame(user);
        if (game == null) return 1;
        game.getSocketHandlers().add(socketHandler);
        socketHandler.getLoginController().getLoggedUser().getInvites().remove(user);
        return 0;
    }

    private void start(String command){
        Matcher matcher = getMatcher(regexes[5],command,false);
        socketHandler.getGame().startGame(Integer.parseInt(matcher.group(1)),Integer.parseInt(matcher.group(2)));

    }

}
