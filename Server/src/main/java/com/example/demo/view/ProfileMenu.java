package com.example.demo.view;


import com.google.gson.Gson;
import com.example.demo.model.User;
import com.example.demo.network.MySocketHandler;

import java.util.regex.Matcher;

public class ProfileMenu extends Menu{
    {
        regexes = new String[]{
                "^menu exit$",
                "^menu show-current$",
                "^save (.*)",
                "^menu enter.*",
                "^friend (?<name>\\S+)",
                "^rm (?<name>\\S+)",
                "^update",
                "^getUserList",
                "^friend; (?<name>\\S+)",
        };
    }
    private final String[] fieldRegexes = {
            ".*--current (\\S+).*",
            ".*--new (\\S+).*"
    };

    public ProfileMenu(MySocketHandler socketHandler) {
        super(socketHandler);
    }


    @Override
    protected boolean commands(String command)
    {
        commandNumber = getCommandNumber(command, regexes,false);
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
                changeNickname(command);
            } break;
            case 3:
                System.out.println("menu navigation is not possible");
                break;
            case 4:
                Matcher matcher = getMatcher(regexes[4],command,false);
                socketHandler.send(String.valueOf(socketHandler.getLoginController().sendFriendRequest(matcher.group(1),true)));
                break;
            case 5:
                matcher = getMatcher(regexes[5],command,false);
                socketHandler.send(String.valueOf(socketHandler.getLoginController().remove(matcher.group(1))));
                break;
            case 6:
                socketHandler.send(new Gson().toJson(socketHandler.getLoginController().getLoggedUser()));
                break;
            case 7:
                socketHandler.send(new Gson().toJson(User.getListOfUsers()));
                break;
            case 8:
                matcher = getMatcher(regexes[8],command,false);
                socketHandler.send(String.valueOf(socketHandler.getLoginController().sendFriendRequest(matcher.group(1),false)));
                break;

        }
        return false;
    }
    private void changeNickname(String command)
    {
        Matcher matcher = getMatcher(regexes[2],command,false);
        User user =new Gson().fromJson(matcher.group(1),User.class);
        socketHandler.loginController.save(user);
        socketHandler.send("saved");
    }
}
