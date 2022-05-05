package view;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.mockito.internal.matchers.Null;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class MutatedMenu {
    protected static Scanner scanner;
    protected int nextMenu;
    public int run(Scanner scanner,int menu) {
        nextMenu = menu;
        Menu.scanner = scanner;
        String command;
        JCommander jCommander = jCommander();

        while (true) {
             try {
                 jCommander = jCommander();
                 jCommander.parse(translateCommandline(scanner.nextLine()));
                 String parsedCommand = jCommander.getParsedCommand();
                 JCommander parsedJCommander = jCommander.getCommands().get(parsedCommand);
                 if(parsedJCommander == null)
                 {
                     System.out.println("invalid Command");
                     continue;
                 }
                 Object commandObject = parsedJCommander.getObjects().get(0);
                 int x = ((Runnable)commandObject).run(parsedCommand.toLowerCase(Locale.ROOT));
                 if(nextMenu != x) {
                     nextMenu = x;
                     break;
                 }
             }
             catch (ParameterException e){
                 System.out.println("invalid Command");
             }
            if (gameOver())
                break;
        }
        return nextMenu;
    }

    protected static Matcher getMatcher(String regex, String command) {
        Pattern pattern = Pattern.compile(regex.toLowerCase(Locale.ROOT));
        Matcher matcher = pattern.matcher(command.toLowerCase(Locale.ROOT));
        matcher.find();
        return matcher;
    }
    protected abstract JCommander jCommander();
    protected static int getCommandNumber(String input, String[] commands) {
        for (int i = 0; i < commands.length; i++)
            if (Pattern.compile(commands[i].toLowerCase(Locale.ROOT)).matcher(input.toLowerCase(Locale.ROOT)).matches())
                return i;
        return -1;
    }

    protected String[] regexes;
    protected int commandNumber;
    //abstract protected boolean commands(String command);
    protected boolean gameOver()
    {
        return false;
    }

    public static String[] translateCommandline(String toProcess) {
        if (toProcess == null || toProcess.length() == 0) {
            //no command? no string
            return new String[0];
        }
        // parse with a simple finite state machine

        final int normal = 0;
        final int inQuote = 1;
        final int inDoubleQuote = 2;
        int state = normal;
        final StringTokenizer tok = new StringTokenizer(toProcess, "\"\' ", true);
        final ArrayList<String> result = new ArrayList<String>();
        final StringBuilder current = new StringBuilder();
        boolean lastTokenHasBeenQuoted = false;

        while (tok.hasMoreTokens()) {
            String nextTok = tok.nextToken();
            switch (state) {
                case inQuote:
                    if ("\'".equals(nextTok)) {
                        lastTokenHasBeenQuoted = true;
                        state = normal;
                    } else {
                        current.append(nextTok);
                    }
                    break;
                case inDoubleQuote:
                    if ("\"".equals(nextTok)) {
                        lastTokenHasBeenQuoted = true;
                        state = normal;
                    } else {
                        current.append(nextTok);
                    }
                    break;
                default:
                    if ("\'".equals(nextTok)) {
                        state = inQuote;
                    } else if ("\"".equals(nextTok)) {
                        state = inDoubleQuote;
                    } else if (" ".equals(nextTok)) {
                        if (lastTokenHasBeenQuoted || current.length() != 0) {
                            result.add(current.toString());
                            current.setLength(0);
                        }
                    } else {
                        current.append(nextTok);
                    }
                    lastTokenHasBeenQuoted = false;
                    break;
            }
        }
        if (lastTokenHasBeenQuoted || current.length() != 0) {
            result.add(current.toString());
        }
        if (state == inQuote || state == inDoubleQuote) {
            throw new RuntimeException("unbalanced quotes in " + toProcess);
        }
        return result.toArray(new String[result.size()]);
    }
}
