package controller;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class NetworkController {
    private static Socket socket;
    private static PrintStream printStream;
    private static Scanner scanner;
    public static boolean connect(){
        try {
            socket = new Socket("localhost",69);
            printStream = new PrintStream(socket.getOutputStream());
            scanner = new Scanner(socket.getInputStream());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static PrintStream getPrintStream() {
        return printStream;
    }
}
