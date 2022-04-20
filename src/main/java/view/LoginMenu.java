package view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginMenu extends Menu {

    @Override
    protected boolean commands(String command) {
        regexes = new String[]{
                "^menu exit$",
                "^menu show-current$",
                "^user create$",
                "^user login$"
        };
        fieldRegexes = new String[]{
                "^--username (\\w+)$",
                "^-u (\\w+)$",
                "^--nickname (.+) -$",
                "^-n (.+) -$",
                "^--password (\\W+)$",
                "^-p (\\W+)$"
        };
        command_number = getCommandNumber(command, regexes);
        boolean isValid = true;
        switch (command_number) {
            case -1:
                System.out.println("invalid command");
            case 0:
                return false;
            case 1:
                System.out.println("Login Menu");
            case 2:
                String[] userRegexes = new String[]{fieldRegexes[0],fieldRegexes[1]};
                String[] nickRegexes = new String[]{fieldRegexes[2],fieldRegexes[3]};
                String[] passRegexes = new String[]{fieldRegexes[4],fieldRegexes[5]};
                String username, password, nickname;
                username = fieldRegexes[getCommandNumber(command, userRegexes)];
                password = fieldRegexes[getCommandNumber(command, passRegexes)];
                nickname = fieldRegexes[getCommandNumber(command, nickRegexes)];
                if(username)



        }

        return true;
    }

    private void createNewUser(String command) {
//print
    }

    private boolean loginUser(String command) {
        return true;
    }
}
