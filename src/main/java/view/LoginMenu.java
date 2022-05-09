package view;


import controller.LoginController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginMenu extends Menu {
    {
        regexes = new String[]{
                "^menu exit$",
                "^menu show-current$",
                "^user create.*",
                "^user login.*",
                "^menu enter.*"
        };
    }

    private final String[] userRegexes = {
            ".*--username (\\w+).*",
            ".*-u (\\w+).*"
    };

    private final String[] nickRegexes = {
            ".*--nickname (.+)-?.*",
            ".*-n (.+)-?.*"
    };

    private final String[] passRegexes = {
            ".*--password (\\S+).*",
            ".*-p (\\S+).*"
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

        commandNumber = getCommandNumber(command, regexes, true);
        switch (commandNumber) {
            case -1:
                System.out.println("invalid command");
                break;
            case 0: {
                nextMenu = -1;
                return true;
            }
            case 1:
                System.out.println("Login Menu");
                break;
            case 2:
                createNewUser(command);
                break;
            case 3:
                if (loginUser(command))
                    return true;
                break;
            case 4:
                System.out.println("please login first");
                break;
        }
        return false;
    }

    private boolean initializeUserPassNick(String command, StringBuffer username,
                                           StringBuffer password, StringBuffer nickname,
                                           boolean isLogin) {
        int UsernameCommandNumber = getCommandNumber(command, userRegexes, false);
        int PasswordCommandNumber = getCommandNumber(command, passRegexes, false);
        int NicknameCommandNumber = getCommandNumber(command, nickRegexes, false);
        if (UsernameCommandNumber == -1 ||
                PasswordCommandNumber == -1 ||
                (!isLogin && NicknameCommandNumber == -1)) {
            System.out.println("invalid command");
            return false;
        }
        Matcher matcher = patterns[UsernameCommandNumber].matcher(command);
        matcher.find();
        username.append(matcher.group(1));
        matcher = patterns[PasswordCommandNumber + 2].matcher(command);
        matcher.find();
        password.append(matcher.group(1));
        if (!isLogin) {
            matcher = patterns[NicknameCommandNumber + 4].matcher(command);
            matcher.find();
            nickname.append(matcher.group(1));
        }
        return true;
    }

    private void createNewUser(String command) {
        StringBuffer username = new StringBuffer(),
                password = new StringBuffer(),
                nickname = new StringBuffer();
        if (!initializeUserPassNick(command, username, password, nickname, false))
            return;
        int outputNumber = LoginController.createNewUser(username.toString(),
                password.toString(), nickname.toString());
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
            case 3:
                System.out.println("the password is not strong enough");
        }
    }

    private boolean loginUser(String command) {
        StringBuffer username = new StringBuffer(),
                password = new StringBuffer(),
                nickname = new StringBuffer();
        if (!initializeUserPassNick(command, username, password, nickname, true))
            return false;
        int outputNumber = LoginController.loginUser(username.toString(), password.toString());
        switch (outputNumber) {
            case 0:
                System.out.println("user logged in successfully!");
                nextMenu = 1;
                return true;
            case 1, 2:
                System.out.println("Username and password do not match!");
                return false;
        }
        return true;
    }
}