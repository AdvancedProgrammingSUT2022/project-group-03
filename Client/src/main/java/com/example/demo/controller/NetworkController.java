package com.example.demo.controller;

import com.example.demo.HelloApplication;
import com.example.demo.controller.gameController.GameController;
import com.example.demo.model.Response;
import com.example.demo.view.StageController;
import javafx.scene.control.Alert;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkController {
    private static Socket socket;
    private static ObjectInputStream objectInputStream;
    private static PrintStream printStream;
    private static String token;
    public synchronized static boolean connect() {
        try {
            socket = new Socket("localhost", HelloApplication.SERVER_PORT);
             objectInputStream = new ObjectInputStream(socket.getInputStream());
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
    public synchronized static String getResponse(boolean necessary){
        Response response = new Response("");
        try {
            response = (Response) objectInputStream.readObject();
        } catch (Exception e) {
            try {
                socket.close();
            } catch (IOException ex) {
                System.exit(0);
            }

            e.printStackTrace();
        }
        String string = response.l;
        System.out.println(string);
        if(string.startsWith("***_____***")){
            updateHandler();
        }
        return string;
    }
    public synchronized static String send(String  request){
        if(token!= null) printStream.println(token);
        printStream.println(request);
        return getResponse(true);
    }


    public static void setToken() {
        NetworkController.token = getResponse(true);
    }
    public static void deleteToken() {
        NetworkController.token = null;
    }

    public static Socket getSocket() {
        return socket;
    }
    public static void updateHandler(){
        String update = "n";
        String command = "n";
        try {
            Response response = (Response) objectInputStream.readObject();
            command = response.l;

            Response response2 = (Response) objectInputStream.readObject();
            update = response2.l;
        } catch (Exception e) {
            //e.printStackTrace();
            return;
        }
        if(command.startsWith("errorMaker")){
            Matcher matcher = getMatcher("(.+);;(.+);;(.+)",update,false);
            if(matcher.group(3).equals("i"))
                StageController.errorMaker(matcher.group(1),matcher.group(2), Alert.AlertType.INFORMATION);
            else
                StageController.errorMaker(matcher.group(1),matcher.group(2), Alert.AlertType.ERROR);

        }else if (command.startsWith("notif")){
            Matcher matcher = getMatcher("(.+);;(.+)",update,false);
            Notifications notif = Notifications.create().hideAfter(Duration.seconds(5)).text(matcher.group(2))
                    .title(GameController.getCivilizations().get(GameController.getPlayerTurn()).getUser().getNickname() +
                            " - cycles: " + matcher.group(1));
            notif.show();
        }
    }
    protected static Matcher getMatcher(String regex, String command, boolean toLower) {
        Pattern pattern = Pattern.compile(regex.toLowerCase(Locale.ROOT));
        Matcher matcher = pattern.matcher(command.toLowerCase(Locale.ROOT));
        if (!toLower) {
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(command);
        }
        matcher.find();
        return matcher;
    }

    public static ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }
}
