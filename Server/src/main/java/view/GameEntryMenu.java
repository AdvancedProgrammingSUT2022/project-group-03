package view;

import com.google.gson.Gson;
import model.User;
import network.GameHandler;
import network.MySocketHandler;

import java.nio.channels.NetworkChannel;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class GameEntryMenu extends Menu{
    {
        regexes = new String[]{
                "^menu exit$",
                "^update",
                "^create game",
                "^invite (.+);",
                "^accept invite (.+);",
                "^decline invite (.+);",
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
        commandNumber = getCommandNumber(command, regexes,false);
        switch (commandNumber) {
            case -1:
                socketHandler.send("2");
                System.out.println("invalid command");
                break;
            case 0:
                socketHandler.send("exited successfully");
                nextMenu = 1;
                return true;
            case 1:
                socketHandler.send(new Gson().toJson(socketHandler.getLoginController().getLoggedUser()));
                if(socketHandler.getGame()!= null)
                    socketHandler.send(new Gson().toJson(socketHandler.getGame().getUsers()));
                else
                    socketHandler.send(new Gson().toJson(new ArrayList<User>()));
                break;
            case 2: {
                socketHandler.send(String.valueOf(createGame()));
            } break;
            case 3:
                socketHandler.send(String.valueOf(sendInvite(command)));
                break;
            case 4:
                socketHandler.send(String.valueOf(acceptInvite(command)));
                break;
            case 5:
                socketHandler.send(String.valueOf(decline(command)));
                break;
            case 6:
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
    private int createGame()
    {
        if(socketHandler.getGame() != null) return 1;

        socketHandler.setGame(new GameHandler(socketHandler));
        return 0;
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
        if(user == null) return 2;
        GameHandler game = GameHandler.findGame(user);
        if (game == null) return 1;
        game.getSocketHandlers().add(socketHandler);
        socketHandler.setGame(game);
        socketHandler.getLoginController().getLoggedUser().getInvites().remove(user);
        return 0;
    }
    private int decline(String command){
        Matcher matcher = getMatcher(regexes[5],command,false);
        User user = User.findUser(matcher.group(1),false);
        socketHandler.getLoginController().getLoggedUser().getInvites().remove(user);
        return 0;
    }

    private void start(String command){
        Matcher matcher = getMatcher(regexes[6],command,false);
        socketHandler.getGame().startGame(Integer.parseInt(matcher.group(1)),Integer.parseInt(matcher.group(2)));

    }

}
