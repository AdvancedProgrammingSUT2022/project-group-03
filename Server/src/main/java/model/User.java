package model;

import view.UserIcon;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class User implements Serializable {
    private static ArrayList<User> listOfUsers = new ArrayList<>();
    private UserIcon icon;
    private final String username;
    private String password;
    private String nickname;
    private String customAvatar;
    public boolean isOnline;
    private int score;
    private Date lastWin;
    private Date lastOnline;
    private final ArrayList<User> invites = new ArrayList<>();
    private final ArrayList<User> friendsRequest = new ArrayList<>();
    private final ArrayList<User> friends = new ArrayList<>();
    public UserIcon getIcon() {
        return icon;
    }



    public static User findUser(String string, boolean isNickname) {
        for (User listOfUser : listOfUsers)
            if ((!isNickname && Objects.equals(listOfUser.username, string)) ||
                    (isNickname && Objects.equals(listOfUser.nickname, string)))
                return listOfUser;
        return null;
    }
    public static void saveData() {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter("dataBase/users.json");
            fileWriter.write(new Gson().toJson(listOfUsers));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User(String username, String password, String nickname, boolean shouldSave) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.score = 0;
        icon = UserIcon.randomIcon();
        listOfUsers.add(this);
        if (shouldSave)
            saveData();
    }

    public boolean isPasswordCorrect(String password) {
        return this.password.equals(password);
    }

    public void changeNickname(String newNickname) {
        this.nickname = newNickname;
        saveData();
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
        saveData();
    }

    public String getNickname() {
        return nickname;
    }

    public static void deleteUser(User user) {
        if (user == null)
            return;
        listOfUsers.remove(user);
        saveData();
    }

    public int getScore() {
        return score;
    }

    public void setCustomAvatar(String customAvatar) {
        this.customAvatar = customAvatar;
    }

    public void setIcon(UserIcon icon) {
        this.icon = icon;
        saveData();
    }


    public String getUsername() {
        return username;
    }
    public String getLastWin() {
        if(lastWin == null) return "-";
        return lastWin.toString();}
    public Date getLastWinDate() {return lastWin;}
    public Date getLastOnlineDate() {return  lastOnline;}
    public String getLastOnline() {
        if (isOnline) return "online";
        if (lastOnline == null) return "-";
        return lastOnline.toString();
    }

    public void setLastOnline(Date lastOnline) {
        this.lastOnline = lastOnline;
    }

    public void setLastWin(Date lastWin) {
        this.lastWin = lastWin;
    }

    public void setScore(int score) {
        this.score = score;
        saveData();
    }
    public void replace(User user){
        nickname = user.nickname;
        password = user.password;
        icon = user.icon;
        customAvatar = user.customAvatar;
        score = user.score;

    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public ArrayList<User> getFriendsRequest() {
        return friendsRequest;
    }

    public static ArrayList<User> getListOfUsers() {
        return listOfUsers;
    }
}
