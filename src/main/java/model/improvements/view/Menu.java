package model.improvements.view;

import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Menu {
    protected static Scanner scanner;
    protected int nextMenu;
    public int run(Scanner scanner) {
        Menu.scanner = scanner;
        String command;
        while (true) {
            command = scanner.nextLine();
            if (commands(command) || gameOver())
                break;
        }
        return nextMenu;
    }

    protected Matcher getMatcher(String regex, String command) {
        Pattern pattern = Pattern.compile(regex.toLowerCase(Locale.ROOT));
        Matcher matcher = pattern.matcher(command.toLowerCase(Locale.ROOT));
        matcher.find();
        return matcher;
    }
    protected static int getCommandNumber(String input, String[] commands) {
        for (int i = 0; i < commands.length; i++)
            if (Pattern.compile(commands[i].toLowerCase(Locale.ROOT)).matcher(input.toLowerCase(Locale.ROOT)).matches())
                return i;
        return -1;
    }

    protected String[] regexes;
    protected int commandNumber;
    abstract protected boolean commands(String command);
    protected boolean gameOver()
    {
        return false;
    }
}
