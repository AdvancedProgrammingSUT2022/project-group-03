package com.example.demo.model;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private final String sender;
    private final Date date;
    private String content;
    private boolean sent;
    private boolean seen;

    public Message(String sender, Date date, String content) {
        this.sender = sender;
        this.date = date;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public Date getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
