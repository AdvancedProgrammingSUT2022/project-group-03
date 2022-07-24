package network;

import java.util.Scanner;

import view.LoginMenu;
import view.MainMenu;
import view.ProfileMenu;

public class MenuHandler {
    private MySocketHandler mySocketHandler;

    public MySocketHandler getMySocketHandler() {
        return mySocketHandler;
    }

    public void start(Scanner scanner, MySocketHandler mySocketHandler) {
        this.mySocketHandler = mySocketHandler;
        int menu = 0;
        LoginMenu loginMenu = new LoginMenu(mySocketHandler);
        MainMenu mainMenu = new MainMenu(mySocketHandler);
        ProfileMenu profileMenu = new ProfileMenu(mySocketHandler);
        //GameMenu gameMenu = new GameMenu();
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
                    //menu = gameMenu.run(scanner, menu);
                    break;
                default:
                    break;
            }
        }
    }
}
