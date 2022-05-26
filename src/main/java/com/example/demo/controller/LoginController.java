package com.example.demo.controller;

import com.example.demo.model.User;

public class LoginController {
    private static User loggedUser;

    public static int createNewUser(String username,
                                    String password,
                                    String nickname) {
        if (username == null || password == null || nickname == null)
            return 1;
        if (User.findUser(username, false) != null)
            return 2;
        if (User.findUser(nickname, true) != null)
            return 3;
        if (!isPasswordValid(password))
            return 4;
        new User(username, password, nickname);
        return 0;
    }

    public static int loginUser(String username, String password) {
        if (username == null || password == null)
            return 1;
        User tempUser = User.findUser(username, false);
        if (tempUser == null)
            return 2;
        if (!tempUser.isPasswordCorrect(password))
            return 3;
        loggedUser = tempUser;
        return 0;
    }

    public static int changeNickname(String newNickName) {
        User tempUser = User.findUser(newNickName, true);
        if (tempUser != null)
            return 1;
        loggedUser.changeNickname(newNickName);
        return 0;
    }

    public static int changePassword(String currentPassword,
                                     String newPassword) {
        if (!loggedUser.isPasswordCorrect(currentPassword))
            return 1;
        if (loggedUser.isPasswordCorrect(newPassword))
            return 2;
        if (!isPasswordValid(newPassword))
            return 3;
        loggedUser.changePassword(newPassword);
        return 0;
    }

    private static boolean isPasswordValid(String password) {
        if (password.length() < 8 ||
                password.length() > 32)
            return false;
        boolean has_weird_characters = false,
                has_small_letter = false,
                has_big_letter = false,
                has_numbers = false;
        String weird_characters = "*.!#@$%^&(){}[]:;<>,?/~_+-=|";
        for (int i = 0; i < password.length(); i++)
            if (password.charAt(i) >= 'a' &&
                    password.charAt(i) <= 'z') {
                has_small_letter = true;
                break;
            }
        if (!has_small_letter)
            return false;
        for (int i = 0; i < password.length(); i++)
            if (password.charAt(i) >= 'A' &&
                    password.charAt(i) <= 'Z') {
                has_big_letter = true;
                break;
            }
        if (!has_big_letter)
            return false;
        for (int i = 0; i < weird_characters.length(); i++)
            if (password.contains("" + weird_characters.charAt(i))) {
                has_weird_characters = true;
                break;
            }
        if (!has_weird_characters)
            return false;
        for (int i = 0; i < password.length(); i++)
            if (password.charAt(i) >= '0' &&
                    password.charAt(i) <= '9') {
                has_numbers = true;
                break;
            }
        return has_numbers;
    }

    public static User getLoggedUser() {
        return loggedUser;
    }
}

