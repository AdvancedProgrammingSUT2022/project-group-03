package com.example.demo.controller;

import com.example.demo.HelloApplication;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class NetworkController {
    private static Socket socket;
    private static Scanner scanner;
    private static PrintStream printStream;
    private static String token;
    public static boolean connect() {
        try {
            socket = new Socket("localhost", HelloApplication.SERVER_PORT);
            scanner = new Scanner(socket.getInputStream());
            printStream = new PrintStream(socket.getOutputStream());


        } catch (IOException ignored) {
            //ignored.printStackTrace();
            return false;
        }
        return true;
    }

    public static PrintStream getPrintStream() {
        return printStream;
    }
    public static String getResponse(){
        while (!scanner.hasNextLine()){

        }
        return scanner.nextLine();
    }
    public static String send(String  request){
        if(token!= null) printStream.println(token);
        printStream.println(request);
        return getResponse();
    }


    public static void setToken() {
        NetworkController.token = scanner.nextLine();
    }
    public static void deleteToken() {
        NetworkController.token = null;
    }

    public static Socket getSocket() {
        return socket;
    }
}
