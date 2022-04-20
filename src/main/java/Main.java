import view.LoginMenu;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int menu = 0;
        Scanner scanner = new Scanner(System.in);
        LoginMenu loginMenu = new LoginMenu();
	    while (menu != -1){
            switch (menu){
                case 0:
                    menu = loginMenu.run(scanner);
                    break;
                case 1:

            }
        }
    }
}
