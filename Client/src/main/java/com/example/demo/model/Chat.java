package com.example.demo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Chat implements Serializable {
    private String name;
    private final List<User> users;
    private final List<Message> messages = new ArrayList<>();

    public Chat(String name, List<User> members) {
        this.name = name;
        this.users = members;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public List<Message> getAllMessages() {
        return messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

}
