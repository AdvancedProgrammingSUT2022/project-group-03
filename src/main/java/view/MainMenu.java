package view;

import model.building.controller.GameController;
import model.building.controller.LoginController;
import model.User;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainMenu extends Menu {

    {
        regexes = new String[]{
                "^menu exit$",
                "^menu show-current$",
                "^user logout$",
                "^play game.*",
                "^menu enter.*$"
        };
    }

    private boolean startGame(String command) {
        String addPlayerRegex = ".*--player1 (\\w+).*";
        int playerNumber = 1;
        ArrayList<User> usersList = new ArrayList<>();
        usersList.add(LoginController.getLoggedUser());
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
            addPlayerRegex = addPlayerRegex.replaceFirst("[0-9]", Integer.toString(playerNumber));
        }
        if(usersList.size() < 2){
            System.out.println("you need to at least choose one player");
            return false;
        }
        GameController.startGame(usersList);
        nextMenu = 3;
        System.out.println(GameController.printMap());
        return true;
    }
    private boolean menuEnter(String command)
    {
        if(!command.startsWith(" profile", 10))
        {
            System.out.println("menu navigation is not possible");
            return false;
        }
        System.out.println("entered profile menu successfully");
        nextMenu=2;
        return true;
    }
    @Override
    protected boolean commands(String command) {

        commandNumber = getCommandNumber(command, regexes,true);
        switch (commandNumber) {
            case -1:
                System.out.println("invalid command");
                break;
            case 0,2:
            {
                System.out.println("exited successfully");
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
                if(menuEnter(command))
                    return true;
                break;
        }
        return false;
    }
}
