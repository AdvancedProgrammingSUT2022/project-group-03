import view.*;
import view.gameMenu.GameMenu;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int menu = 0;
        Scanner scanner = new Scanner(System.in);
        LoginMenu loginMenu = new LoginMenu();
        MainMenu mainMenu = new MainMenu();
        ProfileMenu profileMenu = new ProfileMenu();
        GameMenu gameMenu = new GameMenu();
	    while (menu != -1){
            switch (menu){
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
                    menu = gameMenu.run(scanner,menu);
                    break;
                default: break;
            }
        }
    }
}
