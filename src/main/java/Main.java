import model.Color;
import view.LoginMenu;
import view.MainMenu;
import view.ProfileMenu;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int menu = 0;
        Scanner scanner = new Scanner(System.in);
        LoginMenu loginMenu = new LoginMenu();
        MainMenu mainMenu = new MainMenu();
        ProfileMenu profileMenu = new ProfileMenu();


        StringBuilder mapString = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            mapString.append("   _____        ".repeat(10)).append("\n");
            mapString.append("  /     \\       ".repeat(10)).append("\n");
            mapString.append(" /       \\      ".repeat(10)).append("\n");
            mapString.append("/    #    \\_____".repeat(10)).append("\n");
            mapString.append("\\         /     ".repeat(10)).append("\n");
            mapString.append(" \\       /      ".repeat(10)).append("\n");
            mapString.append("  \\_____/       ".repeat(10)).append("\n");
        }

        System.out.println(mapString);
        System.out.println(mapString);


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
                default: break;
            }
        }
    }
}
