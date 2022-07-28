package com.example.demo.controller;

import com.example.demo.model.Chat;
import com.example.demo.model.User;
import com.example.demo.network.MySocketHandler;
import com.google.gson.Gson;
import com.example.demo.model.ChatPayload;

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

    public synchronized void sendAllUsersToClient() {
        mySocketHandler.send(new Gson().toJson(User.getListOfUsers()));
    }

    public synchronized void sendAllChatsToClient() {
        loadChats();
        List<Chat> relatedChats = new ArrayList<>();
        for (Chat chat : chats) {
            if (chat.getName().equals("Public Chat"))
                relatedChats.add(chat);
            else
                for (User user : chat.getUsers())
                    if (user.getUsername().equals(mySocketHandler.loginController.getLoggedUser().getUsername()))
                        relatedChats.add(chat);
        }
        ChatPayload payload = new ChatPayload("get chats", relatedChats);
        mySocketHandler.send(new Gson().toJson(payload));
    }

    public synchronized void update(Chat chat) {
        loadChats();
        chats.removeIf(chatInServer -> chatInServer.getName().equals(chat.getName()));
        chats.add(chat);
        mySocketHandler.send("ok");
        updateSavedChats();

        System.out.println("\n\nali");
        for (Chat chat2 : chats) {
            System.out.print("chat name: " + chat2.getName() + "  chat members: ");
            for (User user : chat2.getUsers())
                System.out.print(user.getUsername() + ", ");
            System.out.println();
        }
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
//            //add all to the public chat
//            for (User user : User.getListOfUsers())
//                publicChat.addUser(user);
            updateSavedChats();
        }
    }

    private synchronized void updateSavedChats() {
        try {
            FileOutputStream fileStream = new FileOutputStream("dataBase/chats.dat");
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(chats);
            objectStream.flush();
            objectStream.close();
            fileStream.close();
        } catch (Exception e) {
            System.out.println("An Error occurred during saving chats on file.");
            e.printStackTrace();
        }
    }
}
