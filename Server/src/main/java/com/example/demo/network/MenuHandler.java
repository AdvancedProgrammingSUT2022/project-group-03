package com.example.demo.network;

import java.util.Scanner;

import com.example.demo.view.*;
import com.example.demo.view.*;

public class MenuHandler {
    private MySocketHandler mySocketHandler;

    public MySocketHandler getMySocketHandler() {
        return mySocketHandler;
    }
    private GameView gameView;

    public GameView getGameView() {
        return gameView;
    }

    public void start(Scanner scanner, MySocketHandler mySocketHandler) {
        this.mySocketHandler = mySocketHandler;
        int menu = 0;
        LoginMenu loginMenu = new LoginMenu(mySocketHandler);
        MainMenu mainMenu = new MainMenu(mySocketHandler);
        ProfileMenu profileMenu = new ProfileMenu(mySocketHandler);
        GameEntryMenu gameEntryMenu = new GameEntryMenu(mySocketHandler);
        gameView = new GameView(mySocketHandler);


        while (menu != -1) {
            switch (menu) {
                case 0:
                    menu = loginMenu.run(scanner);
                    break;
                case 1:
                    menu = mainMenu.run(scanner);
                    break;
                case 2:
                    menu = profileMenu.run(scanner);
                    break;
                case 3:
                    menu =gameEntryMenu.run(scanner);
                break;
                case 4:
                    menu = gameView.run(scanner);
                    break;
                default:
                    break;
            }
        }
    }
}
