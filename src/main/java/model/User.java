package model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private static ArrayList<User> listOfUsers = new ArrayList<>();
    private final String username;
    private String password;
    private String nickname;
    int score;

    static {
        try {
            String json = new String(Files.readAllBytes(Paths.get("dataBase/users.json")));
            listOfUsers = new Gson().fromJson(json, new TypeToken<List<User>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void saveData(){
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("dataBase/users.json");
            fileWriter.write(new Gson().toJson(listOfUsers));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<User> getListOfUsers() {
        return listOfUsers;
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


}
