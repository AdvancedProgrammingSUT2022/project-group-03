package view;


import com.google.gson.Gson;
import controller.LoginController;
import model.User;
import network.MySocketHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileMenu extends Menu{
    {
        regexes = new String[]{
                "^menu exit$",
                "^menu show-current$",
                "^save (.*)",
                "^menu enter.*"
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
                changeNickname(command);
            } break;
            case 3:
                System.out.println("menu navigation is not possible");
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
