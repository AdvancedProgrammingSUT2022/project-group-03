package com.example.demo.network;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.controller.LoginController;
import com.example.demo.model.Response;
import com.example.demo.model.User;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class MySocketHandler extends Thread{
    private static ArrayList<MySocketHandler> socketHandlers = new ArrayList<>();
    private static ArrayList<User> users = new ArrayList<>();
    private static Random random = new Random();
    private Socket socket;
    private Scanner scanner;
    private ObjectOutputStream objectOutputStream;
    private MenuHandler menuHandler;
    private GameHandler game;
    public LoginController loginController;
    private JWTVerifier verifier;

    public static ArrayList<User> getUsers() {
        return users;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public MenuHandler getMenuHandler() {
        return menuHandler;
    }

    public Socket getSocket() {
        return socket;
    }

    public MySocketHandler(Socket socket) throws IOException {
        this.loginController = new LoginController();
        this.socket = socket;
        scanner = new Scanner(socket.getInputStream());
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

    }
    public void initJWT(User user){
        Date date = new Date();

        date = new Date(date.getTime() + 20000);
        try {
            Algorithm algorithm = Algorithm.HMAC256("s" +user.getNickname()+random.nextInt()+"FW@#");
            send(JWT.create()
                    .withIssuer(String.valueOf(this.getId())).withExpiresAt(date)
                    .sign(algorithm));
            verifier = JWT.require(algorithm)
                    .withIssuer(String.valueOf(this.getId()))
                    .build(); //Reusable verifier instance
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
        }
    }
    public String scanLineWithToken(){
        String line = scanner.nextLine();
        if(verifier != null){
        try {
            verifier.verify(line);
        }catch (JWTVerificationException verificationException){
            // TODO: 7/18/2022
        }
        line = scanner.nextLine();
        }
        System.out.println(line);
        return line;

    }

    public void setGame(GameHandler game) {
        this.game = game;
    }

    public GameHandler getGame() {
        return game;
    }

    public void deleteToken(){
        verifier = null;
    }
    @Override
    public void run(){
        socketHandlers.add(this);
        menuHandler = new MenuHandler();
        menuHandler.start(scanner,this);
        socketHandlers.remove(this);
    }
    public void send(String respond){
        try {
            objectOutputStream.writeObject(new Response(respond));
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public void sendUpdate(String command, String update){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("***_____***\n").append(command).append("\n").append(update).append("\n");
        try {
            objectOutputStream.writeObject((new Response(stringBuilder.toString())));
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Scanner getScanner() {
        return scanner;
    }
}
