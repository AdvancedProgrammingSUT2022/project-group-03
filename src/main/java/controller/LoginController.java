package controller;

import model.User;

public class LoginController {
    private static User loggedUser;

    public static int createNewUser(String username, String password, String nickname) {
        if (User.findUser(username, false) != null)
            return 1;
        if (User.findUser(nickname, true) != null)
            return 2;
        User.getListOfUsers().add(new User(username, password, nickname));
        return 0;
    }

    public static int loginUser(String username, String password) {
        User tempUser = User.findUser(username,false);
        if(tempUser==null)
            return 1;
        if(tempUser.isPasswordCorrect(password))
            return 2;
        loggedUser = tempUser;
        return 0;
    }

    public static int changeNickname(String newNickName) {
        User tempUser = User.findUser(newNickName,true);
        if(tempUser!=null)
            return 1;
        tempUser.changeNickname(newNickName);
        return 0;
    }

    public static int changePassword(String currentPassword, String newPassword) {
        if(!loggedUser.isPasswordCorrect(currentPassword))
            return 1;
        if(loggedUser.isPasswordCorrect(newPassword))
            return 2;
        loggedUser.changePassword(newPassword);
        return 0;
    }

    private static boolean isPasswordValid(String password) {
        return true;
    }


}

