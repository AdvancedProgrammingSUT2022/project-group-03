package view;

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

    protected Matcher getMatcher(String regex, String command,boolean toLower) {
        Pattern pattern = Pattern.compile(regex.toLowerCase(Locale.ROOT));
        Matcher matcher = pattern.matcher(command.toLowerCase(Locale.ROOT));
        if(!toLower)
        {
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(command);
        }
        matcher.find();
        return matcher;
    }
    protected static int getCommandNumber(String input, String[] commands, boolean toLower) {
        for (int i = 0; i < commands.length; i++)
            if ((toLower && Pattern.compile(commands[i].toLowerCase(Locale.ROOT)).matcher(input.toLowerCase(Locale.ROOT)).matches()) ||
                    (!toLower && Pattern.compile(commands[i]).matcher(input).matches()))
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
