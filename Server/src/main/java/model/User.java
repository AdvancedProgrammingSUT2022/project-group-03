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

public class User implements Serializable{
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
    private final ArrayList<User> friends = new ArrayList<>();
    static {
        try {
            String json = new String(Files.readAllBytes(Paths.get("dataBase/users.json")));
            listOfUsers = new Gson().fromJson(json, new TypeToken<List<User>>() {
            }.getType());
        } catch (IOException e) {
            File file = new File("dataBase/users.json");
            try {
                file.createNewFile();
                listOfUsers = new ArrayList<>();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
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


    public static User findUser(String string, boolean isNickname) {
        for (User listOfUser : listOfUsers)
            if ((!isNickname && Objects.equals(listOfUser.username, string)) ||
                    (isNickname && Objects.equals(listOfUser.nickname, string)))
                return listOfUser;
        return null;
    }

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        isOnline = false;
        this.score = 0;
        icon = UserIcon.randomIcon();
        listOfUsers.add(this);
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

    public void setCustomAvatar(String customAvatar) {
        this.customAvatar = customAvatar;
    }

    public void setIcon(UserIcon icon) {
        this.icon = icon;
    }

    public String getAvatar() {
        if (icon == UserIcon.CUSTOM) return customAvatar;
        //@Todo ,..,.,
        //return Game.class.getResource(icon.getImage()).toExternalForm();
        return null;
    }
    public void replace(User user){
        nickname = user.nickname;
        password = user.password;
        icon = user.icon;
        customAvatar = user.customAvatar;
        score = user.score;

    }

    public static ArrayList<User> getListOfUsers() {
        return listOfUsers;
    }

    public void setLastOnline(Date lastOnline) {
        this.lastOnline = lastOnline;
    }

    public void setLastWin(Date lastWin) {
        this.lastWin = lastWin;
    }

    public ArrayList<User> getInvites() {
        return invites;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public String getUsername() {
        return username;
    }
}
