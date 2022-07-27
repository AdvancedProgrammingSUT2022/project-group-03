package controller;

import model.User;
import network.MySocketHandler;
import view.UserIcon;
import com.google.gson.Gson;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class LoginController {
    private  User loggedUser;

    public  void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }
    public  void logout() {
        this.loggedUser.isOnline = false;
        this.loggedUser.setLastOnline(new Date());
        MySocketHandler.getUsers().remove(loggedUser);
        this.loggedUser = null;
    }

    public  int createNewUser(String username,
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
        new User(username, password, nickname,true);
        return 0;
    }

    public int loginUser(String username, String password) {
        if (username == null || password == null)
            return 1;
        User tempUser = User.findUser(username, false);
        if (tempUser == null)
            return 2;
        if (!tempUser.isPasswordCorrect(password))
            return 3;
        loggedUser = tempUser;
        MySocketHandler.getUsers().add(tempUser);
        return 0;
    }


    public  int changeData(String currentPassword,
                                     String newPassword,String nickname,String path) {
        if(path != null){
            loggedUser.setIcon(UserIcon.CUSTOM);
             loggedUser.setCustomAvatar(path);
        }
        if (!loggedUser.isPasswordCorrect(currentPassword))
            return 1;
        if(!newPassword.equals("")) {
            if (loggedUser.isPasswordCorrect(newPassword))
                return 2;
            if (!isPasswordValid(newPassword))
                return 3;
            loggedUser.changePassword(newPassword);
        }
        if(!nickname.equals("")){
            if(User.findUser(nickname,true) == null){
                loggedUser.changeNickname(nickname);
            }else return 4;
        }
        return 0;
    }

    private  boolean isPasswordValid(String password) {
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
    public  void save(User user){
        User.deleteUser(loggedUser);
        loggedUser = user;
        User.getListOfUsers().add(loggedUser);
        User.saveData();
    }
    public int sendFriendRequest(String username,boolean accept){
        User user = User.findUser(username,false);
        if(user == null) return 3;
        if(user == loggedUser) return 5;
        if (loggedUser.getFriends().contains(user)) return 2;
        if(!accept &&user.getFriendsRequest().contains(loggedUser)) return 1;
        if(loggedUser.getFriendsRequest().contains(user)){
            loggedUser.getFriendsRequestOG().remove(user.getUsername());
            user.getFriendsRequestOG().remove(loggedUser.getUsername());
            loggedUser.getFriendsOG().add(user.getUsername());
            user.getFriendsOG().add(loggedUser.getUsername());
            User.saveData();
            return 4;
        }
        user.getFriendsRequestOG().add(loggedUser.getUsername());
        User.saveData();
        return 0;
    }

    public int remove(String username){
        User user = User.findUser(username,false);
        if(user == null) return 3;
        loggedUser.getFriendsRequestOG().remove(user.getUsername());
        return 0;
    }
    public  User getLoggedUser() {
        return loggedUser;
    }

}

