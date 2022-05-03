import model.improvements.view.GameMenu;
import model.improvements.view.LoginMenu;
import model.improvements.view.MainMenu;
import model.improvements.view.ProfileMenu;

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
                    menu = gameMenu.run(scanner);
                    break;
                default: break;
            }
        }
    }
}
