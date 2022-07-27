package com.example.demo.view;


import com.google.gson.Gson;
import com.example.demo.model.User;
import com.example.demo.network.MySocketHandler;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainMenu extends Menu {
    private boolean locked = false;

    {
        regexes = new String[]{
                "^menu exit$",
                "^menu show-current$",
                "^user logout$",
                "^play game.*",
                "^menu enter.*$",
                "^friend (?<name>\\S*)",
                "^update"
        };
    }

    public MainMenu(MySocketHandler mySocketHandler) {
        super(mySocketHandler);
    }

    private boolean startGame(String command) {
        String addPlayerRegex = ".*--player1 (\\w+).*";
        int playerNumber = 1;
        ArrayList<User> usersList = new ArrayList<>();
        usersList.add(socketHandler.loginController.getLoggedUser());
        while (Pattern.compile(addPlayerRegex).matcher(command).matches()) {
            Matcher matcher = Pattern.compile(addPlayerRegex).matcher(command);
            matcher.find();
            User tempUser = User.findUser(matcher.group(1), false);
            if (tempUser == null) {
                System.out.println("no user with this username exists");
                return false;
            }
            usersList.add(tempUser);
            playerNumber++;
            addPlayerRegex = addPlayerRegex.replaceFirst("[0-9]", Integer.toString(playerNumber));
        }
        if (usersList.size() < 2) {
            System.out.println("you need to at least choose one player");
            return false;
        }
        nextMenu = 3;
        System.out.println(socketHandler.getGame().getGameController().printMap());
        return true;
    }

    private boolean menuEnter(String command) {
        if (command.startsWith("menu enter scoreboard")) {
            locked = true;
            return false;
        }
        nextMenu = 2;
        return true;
    }

    @Override
    protected boolean commands(String command) {

        commandNumber = getCommandNumber(command, regexes, false);
        switch (commandNumber) {
            case -1:
                System.out.println("invalid command");
                break;
            case 0, 2: {
                if (locked){
                    locked = false;
                    socketHandler.send("exited");
                }
                else {
                    socketHandler.send("exited");
                    socketHandler.deleteToken();
                    socketHandler.getLoginController().logout();
                    nextMenu = 0;
                    return true;
                }
            }
            case 1:
                System.out.println("Main Menu");
                break;
            case 3:
                if (startGame(command))
                    return true;
                break;
            case 4:
                if(command.startsWith("menu enter gameEntry")){
                    nextMenu = 3;
                    socketHandler.send("profile menu");
                    return true;
                }
                if (menuEnter(command)) {
                    socketHandler.send("profile menu");
                    return true;
                }else {
                    socketHandler.send("scoreboard");
                    socketHandler.send(new Gson().toJson(User.getListOfUsers()));
                }
                break;
            case 5:
                Matcher matcher = getMatcher(regexes[5],command,false);
                socketHandler.send(String.valueOf(socketHandler.getLoginController().sendFriendRequest(matcher.group(1),false)));
                break;
            case 6:
                socketHandler.send("scoreboard");
                socketHandler.send(new Gson().toJson(User.getListOfUsers()));
                break;


        }
        return false;
    }
}
