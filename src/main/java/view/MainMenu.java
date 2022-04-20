package view;

import controller.GameController;
import model.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainMenu extends Menu {

    {
        regexes = new String[]{
                "^menu exit$",
                "^menu show-current$",
                "^user logout$",
                "play game.*",
                "^menu enter Profile$"
        };
    }

    private boolean startGame(String command) {
        String addPlayerRegex = ".*--player1 (\\w+).*";
        int playerNumber = 1;
        ArrayList<User> usersList = new ArrayList<>();

        while (Pattern.compile(addPlayerRegex).matcher(command).matches())
        {
            Matcher matcher = Pattern.compile(addPlayerRegex).matcher(command);
            matcher.find();
            User tempUser = User.findUser(matcher.group(1),false);
            if(tempUser==null)
            {
                System.out.println("no user with this username exists");
                return false;
            }
            usersList.add(tempUser);
            playerNumber++;
            addPlayerRegex.replaceFirst("[0-9]", Integer.toString(playerNumber));
        }
        GameController.startGame(usersList);
        nextMenu = 3;
        return true;
    }

    @Override
    protected boolean commands(String command) {

        commandNumber = getCommandNumber(command, regexes);
        switch (commandNumber) {
            case -1:
                System.out.println("invalid command");
                break;
            case 0,2:
            {
                nextMenu = 0;
                return true;
            }
            case 1:
                System.out.println("Main Menu");
                break;
            case 3:
                if (startGame(command))
                    return true;
                break;
            case 4:
                nextMenu = 2;
                return true;
        }
        return false;
    }
}
