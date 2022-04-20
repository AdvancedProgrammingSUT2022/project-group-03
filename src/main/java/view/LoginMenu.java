package view;

import controller.LoginController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginMenu extends Menu {
    {
        regexes = new String[]{
                "^menu exit$",
                "^menu show-current$",
                "user create.*",
                "^user login$"
        };
    }

    //    private String username, password, nickname;
    private final String[] userRegexes = {
            ".*--username (\\w+).*",
            ".*-u (\\w+).*"
    };

    private final String[] nickRegexes = {
            ".*--nickname (.+)-?.*",
            ".*-n (.+)-?.*"
    };

    private final String[] passRegexes = {
            ".*--password (\\w+).*",
            ".*-p (\\w+).*"
    };
    private final Pattern[] patterns = {
            Pattern.compile(userRegexes[0]),
            Pattern.compile(userRegexes[1]),
            Pattern.compile(passRegexes[0]),
            Pattern.compile(passRegexes[1]),
            Pattern.compile(nickRegexes[0]),
            Pattern.compile(nickRegexes[1])
    };

    @Override
    protected boolean commands(String command) {

        commandNumber = getCommandNumber(command, regexes);
        switch (commandNumber) {
            case -1:
                System.out.println("invalid command");
                break;
            case 0:
                return false;
            case 1:
                System.out.println("Login Menu");
                break;
            case 2:
                createNewUser(command);
                break;
            case 3:
                if (loginUser(command))
                    return false;
                break;
        }
        return true;
    }

    private void initializeUserPassNick(String command, StringBuffer username, StringBuffer password, StringBuffer nickname) {
        int UsernameCommandNumber = getCommandNumber(command, userRegexes);
        int PasswordCommandNumber = getCommandNumber(command, passRegexes);
        int NicknameCommandNumber = getCommandNumber(command, userRegexes);
        if (UsernameCommandNumber == -1 ||
                PasswordCommandNumber == -1 ||
                NicknameCommandNumber == -1) {
            System.out.println("invalid command");
            return;
        }
        Matcher matcher = patterns[UsernameCommandNumber].matcher(command);
        matcher.find();
        username.append(matcher.group(1));
        matcher = patterns[PasswordCommandNumber + 2].matcher(command);
        matcher.find();
        password.append(matcher.group(1));
        matcher = patterns[NicknameCommandNumber + 4].matcher(command);
        matcher.find();
        nickname.append(matcher.group(1));
    }

    private void createNewUser(String command) {
        StringBuffer username = new StringBuffer(), password = new StringBuffer(), nickname = new StringBuffer();
        initializeUserPassNick(command, username, password, nickname);
        int outputNumber = LoginController.createNewUser(username.toString(), password.toString(), nickname.toString());
        switch (outputNumber) {
            case 0:
                System.out.println("user created successfully!");
                break;
            case 1:
                System.out.println("user with username " + username + " already exists");
                break;
            case 2:
                System.out.println("user with nickname " + nickname + " already exists");
                break;
        }
    }

    private boolean loginUser(String command) {
        StringBuffer username = new StringBuffer(), password = new StringBuffer(), nickname = new StringBuffer();
        initializeUserPassNick(command, username, password, nickname);
        int outputNumber = LoginController.loginUser(username.toString(), password.toString());
        switch (outputNumber) {
            case 0:
                System.out.println("user logged in successfully!");
                nextMenu = 1;
                return true;
            case 1, 2:
                System.out.println("Username and password didn't match!");
                return false;
        }
        return true;
    }
}