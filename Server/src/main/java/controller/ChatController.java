package controller;

import com.google.gson.Gson;
import model.Chat;
import model.ChatPayload;
import model.Payload;
import model.User;
import network.MySocketHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChatController {
    private List<Chat> chats = new ArrayList<>();
    private final MySocketHandler mySocketHandler;
    private final User loggedInUser;

    public ChatController(MySocketHandler mySocketHandler) {
        this.mySocketHandler = mySocketHandler;
        loggedInUser = mySocketHandler.loginController.getLoggedUser();
        loadChats();
    }

    public synchronized void sendAllChatsToClient() {
        loadChats();
        List<Chat> relatedChats = new ArrayList<>();
        for (Chat chat : chats)
            if (chat.getUsers().contains(loggedInUser))
                relatedChats.add(chat);

        relatedChats.add(chats.get(0));

        ChatPayload payload = new ChatPayload("get chats", relatedChats);
        mySocketHandler.send(new Gson().toJson(payload));
    }

    public synchronized void update(Chat chat) {
        loadChats();
        chats.removeIf(chatInServer -> chatInServer.getName().equals(chat.getName()));
        chats.add(chat);
        mySocketHandler.send("ok");
        updateSavedChats();
    }

    private synchronized void loadChats() {
        try {
            FileInputStream fileInputStream = new FileInputStream("dataBase/chats.dat");
            ObjectInputStream objectStream = new ObjectInputStream(fileInputStream);
            chats = (List<Chat>) objectStream.readObject();
            fileInputStream.close();
            objectStream.close();
        } catch (Exception e) {
            System.out.println("There is no file named 'chats.dat', so the program generated one.");
        }

        if (chats.size() == 0) {
            Chat publicChat = new Chat("Public Chat", new ArrayList<>());
            chats.add(publicChat);
            //add all to the public chat
            for (User user : User.getListOfUsers())
                publicChat.addUser(user);
            updateSavedChats();
        }
    }

    private synchronized void updateSavedChats() {
        try {
            FileOutputStream fileStream = new FileOutputStream("dataBase/chats.dat");
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(chats);
            objectStream.close();
            fileStream.close();
        } catch (Exception e) {
            System.out.println("An Error occurred during saving chats on file.");
            e.printStackTrace();
        }
    }
}
