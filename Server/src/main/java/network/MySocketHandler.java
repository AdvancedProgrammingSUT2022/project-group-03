package network;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import controller.LoginController;
import model.User;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class MySocketHandler extends Thread{
    private static ArrayList<MySocketHandler> socketHandlers = new ArrayList<>();
    private static Random random = new Random();
    private Socket socket;
    private Scanner scanner;
    private PrintStream printStream;
    private MenuHandler menuHandler;
    private GameHandler game;
    public LoginController loginController;
    private JWTVerifier verifier;

    public LoginController getLoginController() {
        return loginController;
    }

    public Socket getSocket() {
        return socket;
    }

    public MySocketHandler(Socket socket) throws IOException {
        this.loginController = new LoginController();
        this.socket = socket;
        scanner = new Scanner(socket.getInputStream());
        printStream = new PrintStream(socket.getOutputStream());

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
        printStream.println(respond);
    }
    public void sendUpdate(String command,String update){
        printStream.println("***_____***");
        printStream.println(command);
        printStream.println(update);
        printStream.flush();
    }
    public void newPrinter(){
        try {
            printStream = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Scanner getScanner() {
        return scanner;
    }
}
