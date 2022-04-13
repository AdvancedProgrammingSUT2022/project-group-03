package view;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Menu {

    protected int nextMenu;
    public int run(Scanner scanner) {
        String command;
        while (true) {
            command = scanner.nextLine();
            if (commands(command) || gameOver())
                break;
        }
        return nextMenu;
    }

    protected Matcher getMatcher(String regex, String command) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(command);
        matcher.find();
        return matcher;
    }

    abstract protected boolean commands(String command);

    protected boolean gameOver()
    {
        return false;
    }
}
