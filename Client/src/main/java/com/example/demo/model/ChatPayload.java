package com.example.demo.model;

import java.util.List;

public class ChatPayload {
    private final String header;
    private List<Chat> chats = null;
    private Chat chat = null;
    private Message message = null;

    public ChatPayload(String header) {
        this.header = header;
    }

    public ChatPayload(String header, Message message) {
        this.header = header;
        this.message = message;
    }

    public ChatPayload(String header, Chat chat) {
        this.header = header;
        this.chat = chat;
    }

    public ChatPayload(String header, List<Chat> chats) {
        this.header = header;
        this.chats = chats;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public Chat getChat() {
        return chat;
    }

    public Message getMessage() {
        return message;
    }

    public String getHeader() {
        return header;
    }
}
