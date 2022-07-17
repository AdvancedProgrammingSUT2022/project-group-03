package com.example.demo.view.model;

import com.example.demo.model.Message;
import javafx.scene.text.Text;

public class ChatLabel {

    public ChatLabel(Message message) {
        Text sender = new Text(message.getSender());
        sender.setStyle("-fx-font-weight: bold; -fx-fill: white; -fx-font-size: 35");

    }
}

