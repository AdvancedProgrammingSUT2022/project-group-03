package model;

import java.util.ArrayList;
import java.util.Objects;

public class User {
    private static ArrayList<User> listOfUsers;
    private final String username;
    private String password;
    private String nickname;
    int score;

    public static User findUser(String string, boolean isNickname) {
        for (User listOfUser : listOfUsers)
            if ((!isNickname && Objects.equals(listOfUser.username, string)) ||
                    (isNickname && Objects.equals(listOfUser.nickname, string)))
                return listOfUser;
        return null;
    }


    public static void removeAccount(User user)
    {

    }

    public User(String username, String password, String nickname) {
        this.username = username;
    }

    public boolean isPasswordCorrect(String username, String password) {
        return false;
    }

    public void changeNickname(String newNickname) {

    }

    public void changePassword(String currentPassword, String newPassword) {

    }


}
