package model.improvements.view;

import controller.LoginController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileMenu extends Menu{
    {
        regexes = new String[]{
                "^menu exit$",
                "^menu show-current$",
                "^profile change (--nickname|-n) (.+).*",
                "^profile change (--password|-p).*"
        };
    }
    private final String[] fieldRegexes = {
            ".*--current (\\w+).*",
            ".*--new (\\w+).*"
    };


    @Override
    protected boolean commands(String command)
    {
        commandNumber = getCommandNumber(command, regexes);
        switch (commandNumber) {
            case -1:
                System.out.println("invalid command");
                break;
            case 0:
                nextMenu = 1;
                return true;
            case 1:
                System.out.println("Profile Menu");
                break;
            case 2: changeNickname(command); break;
            case 3: changePassword(command); break;
        }
        return false;
    }
    private void changeNickname(String command)
    {
        Matcher matcher = getMatcher(regexes[2],command);
        String newNickname = matcher.group(2);
        int outputNumber = LoginController.changeNickname(newNickname);
        switch (outputNumber)
        {
            case 0: System.out.println("nickname changed successfully!"); break;
            case 1: System.out.println("user with nickname " + newNickname + " already exists"); break;
        }
    }
    private void changePassword(String command)
    {
        if(!Pattern.compile(fieldRegexes[0]).matcher(command).matches() ||
            !Pattern.compile(fieldRegexes[1]).matcher(command).matches())
        {
            System.out.println("invalid command");
            return;
        }
        Matcher matcher = getMatcher(fieldRegexes[0], command);
        String currentPass = matcher.group(1);
        matcher = getMatcher(fieldRegexes[1],command );
        String newPass = matcher.group(1);
        int outputNumber = LoginController.changePassword(currentPass, newPass);
        switch (outputNumber)
        {
            case 0: System.out.println("password changed successfully!"); break;
            case 1: System.out.println("current password is invalid"); break;
            case 2: System.out.println("please enter a new password"); break;
        }
    }

}
