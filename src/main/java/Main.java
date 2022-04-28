import view.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int menu = 0;
        System.out.println(  Color.BLACK_BACKGROUND.toString()+ Color.RED_BOLD_BRIGHT +"SE");
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
